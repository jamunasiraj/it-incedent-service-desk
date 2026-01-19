package lu.isd.isd_api.controller;

import lu.isd.isd_api.entity.AuditLog;
import lu.isd.isd_api.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
public class AuditLogController {

    @Autowired
    private AuditLogService auditLogService;

    // Get all audit logs
    @GetMapping
    public ResponseEntity<List<AuditLog>> getAllAuditLogs() {
        List<AuditLog> auditLogs = auditLogService.getAllAuditLogs();
        return ResponseEntity.ok(auditLogs);
    }

    // Get audit log by ID
    @GetMapping("/{id}")
    public ResponseEntity<AuditLog> getAuditLogById(@PathVariable Long id) {
        AuditLog auditLog = auditLogService.getAuditLogById(id);
        return ResponseEntity.ok(auditLog);
    }

    // Get audit logs by admin username
    @GetMapping("/admin/{adminUsername}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByAdminUsername(@PathVariable String adminUsername) {
        List<AuditLog> auditLogs = auditLogService.getAuditLogsByAdminUsername(adminUsername);
        return ResponseEntity.ok(auditLogs);
    }

    // Get audit logs by target user ID
    @GetMapping("/user/{targetUserId}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByTargetUserId(@PathVariable Long targetUserId) {
        List<AuditLog> auditLogs = auditLogService.getAuditLogsByTargetUserId(targetUserId);
        return ResponseEntity.ok(auditLogs);
    }

    // Create a new audit log (manual entry)
    @PostMapping
    public ResponseEntity<AuditLog> createAuditLog(@RequestBody AuditLog auditLog) {
        AuditLog createdAuditLog = auditLogService.createAuditLog(auditLog);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAuditLog);
    }

    // Update an audit log
    @PutMapping("/{id}")
    public ResponseEntity<AuditLog> updateAuditLog(@PathVariable Long id, @RequestBody AuditLog auditLog) {
        AuditLog updatedAuditLog = auditLogService.updateAuditLog(id, auditLog);
        return ResponseEntity.ok(updatedAuditLog);
    }

    // Delete an audit log
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuditLog(@PathVariable Long id) {
        auditLogService.deleteAuditLog(id);
        return ResponseEntity.noContent().build();
    }
}
