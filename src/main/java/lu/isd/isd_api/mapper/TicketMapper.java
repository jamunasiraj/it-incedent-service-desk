package lu.isd.isd_api.mapper;

import lu.isd.isd_api.dto.OwnerPublicDto;
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

        // Use public owner DTO (no id/password)
        OwnerPublicDto ownerDto = null;
        User owner = ticket.getOwner();

        if (owner != null) {
            ownerDto = new OwnerPublicDto(
                    owner.getUsername(),
                    owner.getRole());
        }

        java.util.List<lu.isd.isd_api.dto.OwnerPublicDto> assigneesList = null;
        if (owner != null && ticket.getAssignees() != null) {
            assigneesList = ticket.getAssignees().stream()
                    .map(u -> new lu.isd.isd_api.dto.OwnerPublicDto(u.getUsername(), u.getRole()))
                    .collect(java.util.stream.Collectors.toList());
        }

        TicketResponseDto dto = new TicketResponseDto(
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getUrgency(),
                ownerDto);

        dto.setAssignees(assigneesList);
        return dto;
    }
}
