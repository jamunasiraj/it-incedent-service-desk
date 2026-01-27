package lu.isd.isd_api.dto.request;

import jakarta.validation.constraints.*;
import lu.isd.isd_api.entity.TicketStatus;
import lu.isd.isd_api.entity.TicketUrgency;

public class TicketCreateRequestDto {

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title can't exceed 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Urgency is required")
    private TicketUrgency urgency;

    @NotNull(message = "Ticket status is required")
    private TicketStatus status;
    // ADDED: remark field
    private String remark;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketUrgency getUrgency() {
        return urgency;
    }

    public void setUrgency(TicketUrgency urgency) {
        this.urgency = urgency;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
