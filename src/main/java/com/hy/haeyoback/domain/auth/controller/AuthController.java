package com.hy.haeyoback.domain.auth.controller;

import com.hy.haeyoback.domain.auth.dto.LoginRequest;
import com.hy.haeyoback.domain.auth.dto.LoginResponse;
import com.hy.haeyoback.domain.auth.service.AuthService;
import com.hy.haeyoback.global.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request.getEmail(), request.getPassword());
        return ApiResponse.success(response);
    }
}
