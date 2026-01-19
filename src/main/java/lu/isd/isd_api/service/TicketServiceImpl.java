package lu.isd.isd_api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lu.isd.isd_api.entity.AuditLog; // <-- ADDED
import lu.isd.isd_api.entity.Ticket;
import lu.isd.isd_api.entity.TicketStatus;
import lu.isd.isd_api.entity.TicketUrgency;
import lu.isd.isd_api.entity.User;
import lu.isd.isd_api.repository.TicketRepository;
import lu.isd.isd_api.repository.UserRepository;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService; // <-- ADDED

    // UPDATED constructor to inject AuditLogService
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

        // ===================== AUDIT LOG =====================
        auditLogService.createAuditLog(
                new AuditLog(
                        getCurrentUsername(),
                        "CREATE_TICKET",
                        savedTicket.getId(),
                        "Ticket created"));
        // =====================================================

        return savedTicket;
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    @Override
    public Ticket updateTicket(Long id, Ticket updatedTicket) {
        Ticket existingTicket = getTicketById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id " + id));

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

        // ===================== AUDIT LOG =====================
        auditLogService.createAuditLog(
                new AuditLog(
                        getCurrentUsername(),
                        "UPDATE_TICKET",
                        savedTicket.getId(),
                        "Ticket updated"));
        // =====================================================

        return savedTicket;
    }

    @Override
    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);

        // ===================== AUDIT LOG =====================
        auditLogService.createAuditLog(
                new AuditLog(
                        getCurrentUsername(),
                        "DELETE_TICKET",
                        id,
                        "Ticket deleted"));
        // =====================================================
    }

    @Override
    public List<Ticket> getTicketsByOwnerUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username " + username));
        return ticketRepository.findByOwner(user);
    }

    // Helper method to get logged-in username
    private String getCurrentUsername() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
    }
}
