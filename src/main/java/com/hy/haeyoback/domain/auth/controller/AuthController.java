package com.hy.haeyoback.domain.auth.controller;

import com.hy.haeyoback.domain.auth.dto.LoginRequest;
import com.hy.haeyoback.domain.auth.dto.LoginResponse;
import com.hy.haeyoback.domain.auth.dto.SignupRequest;
import com.hy.haeyoback.domain.auth.service.AuthService;
import com.hy.haeyoback.domain.user.dto.UserResponse;
import com.hy.haeyoback.domain.user.service.UserService;
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
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ApiResponse<UserResponse> signup(@Valid @RequestBody SignupRequest request) {
        UserResponse response = userService.signup(request.getEmail(), request.getPassword());
        return ApiResponse.success(response);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request.getEmail(), request.getPassword());
        return ApiResponse.success(response);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        return ApiResponse.success(null);
    }
}
