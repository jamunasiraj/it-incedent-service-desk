package lu.isd.isd_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import lu.isd.isd_api.entity.Ticket;
import lu.isd.isd_api.entity.User;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // You can add custom query methods here if needed
    List<Ticket> findByOwner(User owner);

    // Find tickets where a user is an assignee (participant)
    List<Ticket> findByAssigneesContaining(User assignee);
}
