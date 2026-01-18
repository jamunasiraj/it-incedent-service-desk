package lu.isd.isd_api.service;

import java.util.List;
import java.util.Optional;

import lu.isd.isd_api.entity.Ticket;
import lu.isd.isd_api.entity.User;

public interface TicketService {
    Ticket createTicket(Ticket ticket);

    List<Ticket> getAllTickets();

    Optional<Ticket> getTicketById(Long id);

    Ticket updateTicket(Long id, Ticket updatedTicket);

    void deleteTicket(Long id);

    List<Ticket> getTicketsByOwnerUsername(String username);

    List<Ticket> getTicketsByOwner(User owner);

}
