package lu.isd.isd_api.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import lu.isd.isd_api.dto.request.TicketCreateRequestDto;
import lu.isd.isd_api.dto.response.TicketResponseDto;
import lu.isd.isd_api.entity.Ticket;
import lu.isd.isd_api.entity.User;

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

    /**
     * CREATE TICKET
     * One-to-Many relationship:
     * One User (owner) → Many Tickets
     */
    @PostMapping
    public ResponseEntity<TicketResponseDto> createTicket(
            @RequestBody TicketCreateRequestDto request,
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

    /**
     * GET TICKET BY ID
     */
    // @GetMapping("/{id}")
    // public ResponseEntity<TicketResponseDto> getTicketById(@PathVariable Long id)
    // {
    // try {
    // Ticket ticket = ticketService.getTicketById(id);
    // return ResponseEntity.ok(TicketMapper.toDto(ticket));
    // } catch (ResourceNotFoundException e) {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    // }
    // }

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
    public ResponseEntity<TicketResponseDto> updateTicket(
            @PathVariable Long id,
            @RequestBody TicketCreateRequestDto ticketDetails) {

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
