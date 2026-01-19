package lu.isd.isd_api.controller;

import java.util.Map;
import java.util.HashMap;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lu.isd.isd_api.dto.request.TicketCreateRequestDto;
import lu.isd.isd_api.dto.response.TicketResponseDto;
import lu.isd.isd_api.entity.Ticket;
import lu.isd.isd_api.entity.User;
import lu.isd.isd_api.exception.ResourceNotFoundException;
import lu.isd.isd_api.mapper.TicketMapper;
import lu.isd.isd_api.repository.UserRepository;
import lu.isd.isd_api.service.TicketService;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final UserRepository userRepository;

    public TicketController(TicketService ticketService, UserRepository userRepository) {
        this.ticketService = ticketService;
        this.userRepository = userRepository;
    }

    @Operation(summary = "Get tickets assigned to a specific user", description = "Returns the list of tickets where the user is assigned as a participant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of tickets assigned to the user", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TicketResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(example = "{\"message\":\"User not found with id: {userId}\",\"status\":404,\"errors\":{}}")))
    })

    @GetMapping("/assigned-to/{userId}")
    public ResponseEntity<List<TicketResponseDto>> getTicketsAssignedToUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        List<Ticket> tickets = ticketService.getTicketsByAssignee(user);

        List<TicketResponseDto> dtos = tickets.stream()
                .map(TicketMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Assign a user to a ticket")
    @ApiResponse(responseCode = "200", description = "User assigned successfully")
    @PostMapping("/{ticketId}/assign/{userId}")
    public ResponseEntity<Map<String, String>> assignUserToTicket(@PathVariable Long ticketId,
            @PathVariable Long userId) {
        ticketService.assignUserToTicket(ticketId, userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User assigned successfully");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Remove a user from a ticket")
    @ApiResponse(responseCode = "204", description = "User removed successfully")
    @DeleteMapping("/{ticketId}/assign/{userId}")
    public ResponseEntity<Void> removeUserFromTicket(
            @PathVariable Long ticketId, @PathVariable Long userId) {

        ticketService.removeUserFromTicket(ticketId, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * CREATE TICKET
     * One-to-Many relationship:
     * One User (owner) → Many Tickets
     */
    @PostMapping
    public ResponseEntity<TicketResponseDto> createTicket(
            @Valid @RequestBody TicketCreateRequestDto request,
            Principal principal) {

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Ticket ticket = new Ticket();
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setUrgency(request.getUrgency());

        // Owner assignment establishes One-to-Many relation
        ticket.setOwner(user);

        Ticket createdTicket = ticketService.createTicket(ticket);
        return ResponseEntity.ok(TicketMapper.toDto(createdTicket));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDto> getTicketById(@PathVariable Long id) {
        Ticket ticket = ticketService.getTicketById(id);
        return ResponseEntity.ok(TicketMapper.toDto(ticket));
    }

    /**
     * GET TICKETS
     * - ADMIN → sees all tickets
     * - USER → sees only owned tickets
     */
    @GetMapping
    public ResponseEntity<List<TicketResponseDto>> getTickets(Principal principal) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<Ticket> tickets;

        if (isAdmin) {
            // Admin access: view all tickets
            tickets = ticketService.getAllTickets();
        } else {
            // User access: view only owned tickets
            User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            tickets = ticketService.getTicketsByOwner(user);
        }

        return ResponseEntity.ok(
                tickets.stream()
                        .map(TicketMapper::toDto)
                        .collect(Collectors.toList()));
    }

    /**
     * UPDATE TICKET
     * Updates basic fields only (title, description, urgency)
     */
    @PutMapping("/{id}")
    public ResponseEntity<TicketResponseDto> updateTicket(@PathVariable Long id,
            @Valid @RequestBody TicketCreateRequestDto ticketDetails) {

        try {
            Ticket toUpdate = new Ticket();
            toUpdate.setTitle(ticketDetails.getTitle());
            toUpdate.setDescription(ticketDetails.getDescription());
            toUpdate.setUrgency(ticketDetails.getUrgency());

            Ticket updatedTicket = ticketService.updateTicket(id, toUpdate);
            return ResponseEntity.ok(TicketMapper.toDto(updatedTicket));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE TICKET
     * Audit log is generated in service layer
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        try {
            ticketService.deleteTicket(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // =========================================================
    // OPTIONAL EXTENSIONS (DO NOT ENABLE NOW)
    // =========================================================

    /*
     * FUTURE: Get tickets where logged-in user is an ASSIGNEE
     * (Many-to-Many relationship)
     *
     * Endpoint idea:
     * GET /api/tickets/assigned
     *
     * Uses:
     * ticketRepository.findByAssigneesContaining(user)
     */

    /*
     * FUTURE: Assign user to ticket
     * POST /api/tickets/{id}/assign/{userId}
     */

    /*
     * FUTURE: Remove user from ticket
     * DELETE /api/tickets/{id}/assign/{userId}
     */
}
