package lu.isd.isd_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lu.isd.isd_api.entity.TicketUrgency;
import lu.isd.isd_api.entity.TicketStatus;

public class TicketUpdateRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Status is required")
    private TicketStatus status;

    @NotNull(message = "Urgency is required")
    private TicketUrgency urgency;
    // remark
    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    // Getters and setters

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

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public TicketUrgency getUrgency() {
        return urgency;
    }

    public void setUrgency(TicketUrgency urgency) {
        this.urgency = urgency;
    }
}
