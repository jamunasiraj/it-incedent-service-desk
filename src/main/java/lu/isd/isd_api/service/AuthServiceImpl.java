package lu.isd.isd_api.service;

import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lu.isd.isd_api.dto.request.LoginRequestDto;
import lu.isd.isd_api.dto.request.RegisterRequestDto;
import lu.isd.isd_api.dto.response.ApiResponseDto;
import lu.isd.isd_api.dto.response.JwtResponseDto;
import lu.isd.isd_api.entity.RoleName;
import lu.isd.isd_api.entity.User;
import lu.isd.isd_api.exception.InvalidCredentialsException;
import lu.isd.isd_api.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public ApiResponseDto registerUser(RegisterRequestDto request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return new ApiResponseDto(false, "Username is already taken!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return new ApiResponseDto(false, "Email is already in use!");
        }

        // EDIT: Improve role parsing to accept variants like TEAM_LEAD, team-lead,
        // "team lead"
        RoleName role = RoleName.USER;
        if (request.getRole() != null && !request.getRole().isBlank()) {
            String raw = request.getRole();
            try {
                role = RoleName.valueOf(raw.toUpperCase());
            } catch (Exception ex) {
                // Try relaxed matching: strip non-alphanumeric and compare
                String normalized = raw.toUpperCase().replaceAll("[^A-Z0-9]", "");
                for (RoleName rn : RoleName.values()) {
                    String rnNorm = rn.name().toUpperCase().replaceAll("[^A-Z0-9]", "");
                    if (rnNorm.equals(normalized)) {
                        role = rn;
                        break;
                    }
                }
            }

        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(role);

        userRepository.save(user);

        return new ApiResponseDto(true, "User registered successfully");
    }

    @Override
    public JwtResponseDto loginUser(LoginRequestDto request) {

        // 1. Find user by username, throw if not found
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

        // 2. Check password matches
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        // 3. Generate JWT token
        String token = jwtService.generateToken(
                user.getId(),
                user.getUsername(),
                user.getRole().name());

        // 4. Prepare roles set
        Set<String> roles = Set.of(user.getRole().name());

        // 5. Return response
        return new JwtResponseDto(token, user.getUsername(), roles);
    }
}
