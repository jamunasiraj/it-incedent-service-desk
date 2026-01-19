package lu.isd.isd_api.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String adminUsername;

    private String action;

    private Long targetUserId;

    private LocalDateTime createdAt = LocalDateTime.now();

    private String details;

    public AuditLog() {
    }

    public AuditLog(String adminUsername, String action, Long targetUserId, String details) {
        this.adminUsername = adminUsername;
        this.action = action;
        this.targetUserId = targetUserId;
        this.details = details;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public String getAction() {
        return action;
    }

    public Long getTargetUserId() {
        return targetUserId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getDetails() {
        return details;
    }
}
