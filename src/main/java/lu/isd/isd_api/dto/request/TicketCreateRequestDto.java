package lu.isd.isd_api.dto.request;

import lu.isd.isd_api.entity.TicketUrgency;

public class TicketCreateRequestDto {

    private String title;
    private String description;
    private TicketUrgency urgency;

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
}
