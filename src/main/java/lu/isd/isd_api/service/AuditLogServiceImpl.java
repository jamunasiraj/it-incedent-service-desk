package lu.isd.isd_api.service;

import lu.isd.isd_api.entity.AuditLog;
import lu.isd.isd_api.exception.ResourceNotFoundException;
import lu.isd.isd_api.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Override
    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }

    @Override
    public AuditLog getAuditLogById(Long id) {
        return auditLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AuditLog", id));
    }

    @Override
    public List<AuditLog> getAuditLogsByAdminUsername(String adminUsername) {
        List<AuditLog> logs = auditLogRepository.findByAdminUsername(adminUsername);
        if (logs.isEmpty()) {
            throw new ResourceNotFoundException("AuditLogs for admin username", adminUsername);
        }
        return logs;
    }

    @Override
    public List<AuditLog> getAuditLogsByTargetUserId(Long targetUserId) {
        List<AuditLog> logs = auditLogRepository.findByTargetUserId(targetUserId);
        if (logs.isEmpty()) {
            throw new ResourceNotFoundException("AuditLogs for target user id", targetUserId);
        }
        return logs;
    }

    @Override
    public AuditLog createAuditLog(AuditLog auditLog) {
        return auditLogRepository.save(auditLog);
    }

    @Override
    public AuditLog updateAuditLog(Long id, AuditLog auditLogDetails) {
        return auditLogRepository.findById(id)
                .map(auditLog -> {
                    auditLog.setDetails(auditLogDetails.getDetails());
                    return auditLogRepository.save(auditLog);
                })
                .orElseThrow(() -> new ResourceNotFoundException("AuditLog", id));
    }

    @Override
    public void deleteAuditLog(Long id) {
        if (!auditLogRepository.existsById(id)) {
            throw new ResourceNotFoundException("AuditLog", id);
        }
        auditLogRepository.deleteById(id);
    }
}
