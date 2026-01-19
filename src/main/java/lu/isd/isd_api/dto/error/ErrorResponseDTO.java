package lu.isd.isd_api.dto.error;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponseDTO {
    private int status;
    private String message;
    private LocalDateTime timeStamp;
    private Map<String, String> errors;

    public ErrorResponseDTO(int status, String message) {
        this.status = status;
        this.message = message;
        this.timeStamp = LocalDateTime.now();
    }

    // Constructor with errors(for validation errors)
    public ErrorResponseDTO(int status, String message, Map<String, String> errors) {
        this.message = message;
        this.status = status;
        this.timeStamp = LocalDateTime.now();
        this.errors = errors;
    }

    // gtters and Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

}
