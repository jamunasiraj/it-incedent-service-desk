package lu.isd.isd_api.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TicketUrgency {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL;

    // EDIT: Accept case-insensitive deserialization from JSON (e.g. "High", "high")
    @JsonCreator
    public static TicketUrgency fromString(String value) {
        if (value == null)
            return null;
        String normalized = value.trim().replaceAll("[^A-Za-z0-9]", "").toUpperCase();
        for (TicketUrgency u : values()) {
            if (u.name().equalsIgnoreCase(normalized) || u.name().replaceAll("[^A-Z0-9]", "").equals(normalized)) {
                return u;
            }
        }
        throw new IllegalArgumentException("Unknown TicketUrgency: " + value);
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }

}
