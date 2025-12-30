package com.hy.haeyoback.domain.auth.controller;

import com.hy.haeyoback.domain.auth.dto.AuthTokens;
import com.hy.haeyoback.domain.auth.dto.LoginRequest;
import com.hy.haeyoback.domain.auth.dto.SignupRequest;
import com.hy.haeyoback.domain.auth.service.AuthService;
import com.hy.haeyoback.domain.user.service.UserService;
import com.hy.haeyoback.global.api.ApiResponse;
import com.hy.haeyoback.global.config.CookieProperties;
import com.hy.haeyoback.global.exception.CustomException;
import com.hy.haeyoback.global.exception.ErrorCode;
import com.hy.haeyoback.global.security.JwtProvider;
import jakarta.validation.Valid;
import java.time.Duration;
import java.time.Instant;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final CookieProperties cookieProperties;

    public AuthController(
            AuthService authService,
            UserService userService,
            JwtProvider jwtProvider,
            CookieProperties cookieProperties
    ) {
        this.authService = authService;
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.cookieProperties = cookieProperties;
    }

    @PostMapping("/signup")
    public ApiResponse<Void> signup(@Valid @RequestBody SignupRequest request) {
        userService.signup(request.getEmail(), request.getPassword());
        return ApiResponse.successMessage("Signup successful");
    }

    @PostMapping("/login")
    public ApiResponse<Void> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        AuthTokens tokens = authService.login(request.getEmail(), request.getPassword());
        setRefreshTokenCookie(response, tokens.getRefreshToken());
        response.setHeader("Authorization", "Bearer " + tokens.getAccessToken());
        return ApiResponse.successMessage("Login successful");
    }

    @PostMapping("/refresh")
    public ApiResponse<Void> refresh(
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "Refresh token is required");
        }
        AuthTokens tokens = authService.refresh(refreshToken);
        setRefreshTokenCookie(response, tokens.getRefreshToken());
        response.setHeader("Authorization", "Bearer " + tokens.getAccessToken());
        return ApiResponse.successMessage("Token refreshed");
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (refreshToken != null && !refreshToken.isBlank()) {
            authService.logout(refreshToken);
        }
        clearRefreshTokenCookie(response);
        return ApiResponse.successMessage("Logout successful");
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        JwtProvider.RefreshTokenInfo info = jwtProvider.parseRefreshToken(refreshToken);
        long maxAge = Duration.between(Instant.now(), info.expiresAt()).getSeconds();
        if (maxAge < 0) {
            maxAge = 0;
        }
        ResponseCookie cookie = ResponseCookie.from(cookieProperties.getName(), refreshToken)
                .httpOnly(cookieProperties.isHttpOnly())
                .secure(cookieProperties.isSecure())
                .sameSite(cookieProperties.getSameSite())
                .path(cookieProperties.getPath())
                .maxAge(maxAge)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    private void clearRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(cookieProperties.getName(), "")
                .httpOnly(cookieProperties.isHttpOnly())
                .secure(cookieProperties.isSecure())
                .sameSite(cookieProperties.getSameSite())
                .path(cookieProperties.getPath())
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}
