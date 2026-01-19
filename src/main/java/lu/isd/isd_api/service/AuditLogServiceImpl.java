package lu.isd.isd_api.service;

import lu.isd.isd_api.entity.AuditLog;
import lu.isd.isd_api.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Override
    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }

    @Override
    public Optional<AuditLog> getAuditLogById(Long id) {
        return auditLogRepository.findById(id);
    }

    @Override
    public List<AuditLog> getAuditLogsByAdminUsername(String adminUsername) {
        return auditLogRepository.findByAdminUsername(adminUsername);
    }

    @Override
    public List<AuditLog> getAuditLogsByTargetUserId(Long targetUserId) {
        return auditLogRepository.findByTargetUserId(targetUserId);
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
                .orElseThrow(() -> new RuntimeException("AuditLog not found with id: " + id));
    }

    @Override
    public void deleteAuditLog(Long id) {
        auditLogRepository.deleteById(id);
    }
}
