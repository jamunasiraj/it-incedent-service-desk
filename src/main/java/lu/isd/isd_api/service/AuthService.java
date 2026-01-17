package lu.isd.isd_api.service;

import lu.isd.isd_api.dto.request.RegisterRequestDto;

import org.springframework.stereotype.Service;

import lu.isd.isd_api.dto.request.LoginRequestDto;
import lu.isd.isd_api.dto.response.ApiResponseDto;
import lu.isd.isd_api.dto.response.JwtResponseDto;

@Service
public interface AuthService {
    ApiResponseDto registerUser(RegisterRequestDto request);

    JwtResponseDto loginUser(LoginRequestDto request);
}
