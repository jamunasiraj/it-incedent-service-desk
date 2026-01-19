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
import lu.isd.isd_api.dto.OwnerDto;
import lu.isd.isd_api.dto.request.AdminUpdateUserRequest;
import lu.isd.isd_api.entity.User;
import lu.isd.isd_api.exception.ResourceNotFoundException;
import lu.isd.isd_api.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuditLogService auditLogService;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));
    }

    @Override
    public void adminUpdateUser(Long userId, AdminUpdateUserRequest request) {
        User user = getUserById(userId);

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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String admin = (auth != null) ? auth.getName() : "system";

        String details = "updated fields:" +
                (request.getUsername() != null ? " username=" + request.getUsername() : "") +
                (request.getEmail() != null ? " email=" + request.getEmail() : "") +
                (request.getRole() != null ? " role=" + request.getRole() : "");

        auditLogService.createAuditLog(new AuditLog(admin, "ADMIN_UPDATE_USER", userId, details));
    }

    @Override
    public List<OwnerDto> getAllUsers() {
        return userRepository.findAllByDeletedFalse().stream()
                .map(u -> new OwnerDto(u.getId(), u.getUsername(), u.getRole()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long userId) {
        User user = getUserById(userId); // reuse getUserById method

        user.setDeleted(true);
        userRepository.save(user);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String admin = (auth != null) ? auth.getName() : "system";

        auditLogService.createAuditLog(new AuditLog(admin, "DELETE_USER_SOFT", userId, "soft deleted user"));
    }
}
