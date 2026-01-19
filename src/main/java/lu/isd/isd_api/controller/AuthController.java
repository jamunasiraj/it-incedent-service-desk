package lu.isd.isd_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lu.isd.isd_api.dto.request.RegisterRequestDto;
import lu.isd.isd_api.dto.request.LoginRequestDto;
import lu.isd.isd_api.dto.response.ApiResponseDto;
import lu.isd.isd_api.dto.response.JwtResponseDto;
import lu.isd.isd_api.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto> register(@RequestBody RegisterRequestDto request) {
        ApiResponseDto response = authService.registerUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody LoginRequestDto request) {
        JwtResponseDto jwtResponse = authService.loginUser(request);
        return ResponseEntity.ok(jwtResponse);
    }

}
