package lu.isd.isd_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import lu.isd.isd_api.entity.AuditLog;
import lu.isd.isd_api.repository.AuditLogRepository;

import lu.isd.isd_api.dto.OwnerDto;
import lu.isd.isd_api.dto.request.AdminUpdateUserRequest;
import lu.isd.isd_api.entity.User;
import lu.isd.isd_api.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuditLogRepository auditLogRepository;

    public UserServiceImpl(UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserServiceImpl() {
        // default constructor for Spring (field injection)
    }

    // for tests
    public UserServiceImpl(UserRepository userRepository,
            PasswordEncoder passwordEncoder, AuditLogRepository auditLogRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public void adminUpdateUser(Long userId, AdminUpdateUserRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userRepository.save(user);

        // Audit admin update
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String admin = auth != null ? String.valueOf(auth.getName()) : "system";
        String details = "updated fields:" +
                (request.getUsername() != null ? " username=" + request.getUsername() : "") +
                (request.getEmail() != null ? " email=" + request.getEmail() : "") +
                (request.getRole() != null ? " role=" + request.getRole() : "");
        AuditLog log = new AuditLog(admin, "ADMIN_UPDATE_USER", userId, details);
        auditLogRepository.save(log);
    }

    @Override
    public List<OwnerDto> getAllUsers() {
        return userRepository.findAllByDeletedFalse().stream()
                .map(u -> new OwnerDto(u.getId(), u.getUsername(), u.getRole()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setDeleted(true);
        userRepository.save(user);

        // Audit
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String admin = auth != null ? String.valueOf(auth.getName()) : "system";
        AuditLog log = new AuditLog(admin, "DELETE_USER_SOFT", userId, "soft deleted user");
        auditLogRepository.save(log);
    }
}
