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

        RoleName role = RoleName.USER;
        try {
            role = RoleName.valueOf(request.getRole().toUpperCase());
        } catch (Exception e) {
            // keep default
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
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        // 2. Check password matches
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        // 3. Generate real JWT token using JwtService
        // Pass user ID, username, and role name to create JWT claims
        String token = jwtService.generateToken(user.getId(), user.getUsername(), user.getRole().name());

        // 4. Prepare roles set for response
        Set<String> roles = Set.of(user.getRole().name());

        // 5. Return JWT response with real token
        return new JwtResponseDto(token, user.getUsername(), roles);
    }
}
