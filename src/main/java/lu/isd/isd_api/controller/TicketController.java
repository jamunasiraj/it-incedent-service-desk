package lu.isd.isd_api.controller;

// EDIT: Return TicketResponseDto instead of entity to include owner details safely (2026-01-19)

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

    // Create Ticket
    @PostMapping
    public ResponseEntity<TicketResponseDto> createTicket(@RequestBody TicketCreateRequestDto request,
            Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Ticket ticket = new Ticket();
        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setUrgency(request.getUrgency());
        ticket.setOwner(user);

        Ticket createdTicket = ticketService.createTicket(ticket);
        return ResponseEntity.ok(TicketMapper.toDto(createdTicket));
    }

    // Get Ticket by ID
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDto> getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id)
                .map(TicketMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get All Tickets for logged-in user (admins see all, others see their own)
    @GetMapping
    public ResponseEntity<List<TicketResponseDto>> getTickets(Principal principal) {
        // Allow admins to see all tickets; others see their own (2026-01-19)
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<Ticket> tickets;
        if (isAdmin) {
            tickets = ticketService.getAllTickets();
        } else {
            User user = userRepository.findByUsername(principal.getName())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            tickets = ticketService.getTicketsByOwner(user);
        }

        List<TicketResponseDto> dtos = tickets.stream().map(TicketMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Update Ticket by ID
    @PutMapping("/{id}")
    public ResponseEntity<TicketResponseDto> updateTicket(@PathVariable Long id,
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

    // Delete Ticket by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        try {
            ticketService.deleteTicket(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
