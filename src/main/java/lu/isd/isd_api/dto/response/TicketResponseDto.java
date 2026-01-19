package lu.isd.isd_api.dto.response;

import java.time.LocalDateTime;

import lu.isd.isd_api.dto.OwnerDto;
import lu.isd.isd_api.entity.TicketStatus;
import lu.isd.isd_api.entity.TicketUrgency;

public class TicketResponseDto {

    private Long id;
    private String title;
    private String description;
    private TicketStatus status;
    private TicketUrgency urgency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private OwnerDto owner;

    // ✅ Constructor used by Mapper
    public TicketResponseDto(
            Long id,
            String title,
            String description,
            TicketStatus status,
            TicketUrgency urgency,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            OwnerDto owner) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.urgency = urgency;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.owner = owner;
    }

    // ✅ Getters only (recommended for response DTOs)

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public TicketUrgency getUrgency() {
        return urgency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public OwnerDto getOwner() {
        return owner;
    }
}
