package lu.isd.isd_api.dto.response;

import lu.isd.isd_api.dto.OwnerPublicDto;
import lu.isd.isd_api.entity.TicketStatus;
import lu.isd.isd_api.entity.TicketUrgency;

public class TicketResponseDto {

    private Long id;

    private String title;
    private String description;
    private TicketStatus status;
    private TicketUrgency urgency;
    private OwnerPublicDto owner;
    private java.util.List<OwnerPublicDto> assignees;
    // ADDED: remark field
    private String remark;

    public TicketResponseDto(
            String title,
            String description,
            TicketStatus status,
            TicketUrgency urgency,
            String remark,
            OwnerPublicDto owner) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.urgency = urgency;
        this.remark = remark;
        this.owner = owner;
        this.assignees = java.util.Collections.emptyList();

    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public OwnerPublicDto getOwner() {
        return owner;
    }

    public java.util.List<OwnerPublicDto> getAssignees() {
        return assignees;
    }

    public void setAssignees(java.util.List<OwnerPublicDto> assignees) {
        this.assignees = assignees == null ? java.util.Collections.emptyList() : assignees;
    }
}
