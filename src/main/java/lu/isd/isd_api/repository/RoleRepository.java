package lu.isd.isd_api.repository;

import lu.isd.isd_api.entity.Role;
import lu.isd.isd_api.entity.RoleName;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName roleName);

}
