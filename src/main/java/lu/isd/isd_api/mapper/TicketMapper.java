package lu.isd.isd_api.mapper;

import lu.isd.isd_api.dto.OwnerDto;
import lu.isd.isd_api.dto.response.TicketResponseDto;
import lu.isd.isd_api.entity.Ticket;
import lu.isd.isd_api.entity.User;

public class TicketMapper {

    // Prevent instantiation (utility class)
    private TicketMapper() {
    }

    /**
     * Convert Ticket entity to TicketResponseDto
     */
    public static TicketResponseDto toDto(Ticket ticket) {

        if (ticket == null) {
            return null;
        }

        OwnerDto ownerDto = null;
        User owner = ticket.getOwner();

        if (owner != null) {
            ownerDto = new OwnerDto(
                    owner.getId(),
                    owner.getUsername(),
                    owner.getRole());
        }

        return new TicketResponseDto(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getUrgency(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt(),
                ownerDto);
    }
}
