package lu.isd.isd_api.service;

import lu.isd.isd_api.entity.AuditLog;
import java.util.List;
import java.util.Optional;

public interface AuditLogService {
    List<AuditLog> getAllAuditLogs();

    Optional<AuditLog> getAuditLogById(Long id);

    List<AuditLog> getAuditLogsByAdminUsername(String adminUsername);

    List<AuditLog> getAuditLogsByTargetUserId(Long targetUserId);

    AuditLog createAuditLog(AuditLog auditLog);

    AuditLog updateAuditLog(Long id, AuditLog auditLog);

    void deleteAuditLog(Long id);
}
