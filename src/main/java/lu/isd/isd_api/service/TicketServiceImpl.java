package lu.isd.isd_api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lu.isd.isd_api.entity.AuditLog;
import lu.isd.isd_api.entity.Ticket;
import lu.isd.isd_api.entity.TicketStatus;
import lu.isd.isd_api.entity.TicketUrgency;
import lu.isd.isd_api.entity.User;
import lu.isd.isd_api.exception.ResourceNotFoundException; // <-- ADDED
import lu.isd.isd_api.repository.TicketRepository;
import lu.isd.isd_api.repository.UserRepository;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    public TicketServiceImpl(
            TicketRepository ticketRepository,
            UserRepository userRepository,
            AuditLogService auditLogService) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
    }

    @Override
    public List<Ticket> getTicketsByOwner(User owner) {
        return ticketRepository.findByOwner(owner);
    }

    @Override
    public Ticket createTicket(Ticket ticket) {
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setStatus(ticket.getStatus() != null ? ticket.getStatus() : TicketStatus.OPEN);
        ticket.setUrgency(ticket.getUrgency() != null ? ticket.getUrgency() : TicketUrgency.MEDIUM);

        Ticket savedTicket = ticketRepository.save(ticket);

        auditLogService.createAuditLog(new AuditLog(
                getCurrentUsername(),
                "CREATE_TICKET",
                savedTicket.getId(),
                "Ticket created"));

        return savedTicket;
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", id));
    }

    @Override
    public Ticket updateTicket(Long id, Ticket updatedTicket) {

        // ✅ Proper exception instead of RuntimeException
        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", id));

        if (updatedTicket.getTitle() != null) {
            existingTicket.setTitle(updatedTicket.getTitle());
        }
        if (updatedTicket.getDescription() != null) {
            existingTicket.setDescription(updatedTicket.getDescription());
        }
        if (updatedTicket.getStatus() != null) {
            existingTicket.setStatus(updatedTicket.getStatus());
        }
        if (updatedTicket.getUrgency() != null) {
            existingTicket.setUrgency(updatedTicket.getUrgency());
        }

        existingTicket.setUpdatedAt(LocalDateTime.now());

        Ticket savedTicket = ticketRepository.save(existingTicket);

        auditLogService.createAuditLog(new AuditLog(
                getCurrentUsername(),
                "UPDATE_TICKET",
                savedTicket.getId(),
                "Ticket updated"));

        return savedTicket;
    }

    @Override
    public void deleteTicket(Long id) {

        // Ensure ticket exists before delete
        if (!ticketRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ticket", id);
        }

        ticketRepository.deleteById(id);

        auditLogService.createAuditLog(new AuditLog(
                getCurrentUsername(),
                "DELETE_TICKET",
                id,
                "Ticket deleted"));
    }

    @Override
    public List<Ticket> getTicketsByOwnerUsername(String username) {

        // Throw ResourceNotFoundException if user not found by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));

        return ticketRepository.findByOwner(user);
    }

    // Helper method
    private String getCurrentUsername() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        }
        return "system";
    }
}
