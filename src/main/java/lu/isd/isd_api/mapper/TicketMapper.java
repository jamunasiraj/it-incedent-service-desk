package lu.isd.isd_api.mapper;

import lu.isd.isd_api.dto.OwnerPublicDto;
import lu.isd.isd_api.dto.response.TicketResponseDto;
import lu.isd.isd_api.entity.Ticket;
import lu.isd.isd_api.entity.User;

import java.util.List;
import java.util.stream.Collectors;

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

        /* ---------- OWNER ---------- */
        OwnerPublicDto ownerDto = null;
        User owner = ticket.getOwner();

        if (owner != null) {
            ownerDto = new OwnerPublicDto(
                    owner.getUsername(),
                    owner.getRole());
        }

        /* ---------- ASSIGNEES ---------- */
        List<OwnerPublicDto> assigneesList = null;

        if (ticket.getAssignees() != null) {
            assigneesList = ticket.getAssignees().stream()
                    .map(u -> new OwnerPublicDto(
                            u.getUsername(),
                            u.getRole()))
                    .collect(Collectors.toList());
        }

        /* ---------- DTO ---------- */
        TicketResponseDto dto = new TicketResponseDto(
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getUrgency(),
                ownerDto);

        //  set ID
        dto.setId(ticket.getId());

        dto.setAssignees(assigneesList);

        return dto;
    }
}
