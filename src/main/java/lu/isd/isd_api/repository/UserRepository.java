package lu.isd.isd_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lu.isd.isd_api.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by username (for login)
    Optional<User> findByUsername(String username);

    // Find user by email
    Optional<User> findByEmail(String email);

    // Check if username exists (for registration)
    Boolean existsByUsername(String username);

    // Check if email exists
    Boolean existsByEmail(String email);

}
