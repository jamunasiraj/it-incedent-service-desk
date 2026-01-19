package lu.isd.isd_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lu.isd.isd_api.entity.AuditLog;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByAdminUsername(String adminUsername);

    List<AuditLog> findByTargetUserId(Long targetUserId);
}
