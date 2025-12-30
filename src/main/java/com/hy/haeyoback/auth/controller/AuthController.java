package com.hy.haeyoback.auth.controller;

import com.hy.haeyoback.auth.dto.AuthTokens;
import com.hy.haeyoback.auth.dto.LoginRequest;
import com.hy.haeyoback.auth.dto.SignupRequest;
import com.hy.haeyoback.auth.service.AuthService;
import com.hy.haeyoback.user.service.UserService;
import com.hy.haeyoback.global.api.ApiResponse;
import com.hy.haeyoback.global.config.CookieProperties;
import com.hy.haeyoback.global.exception.CustomException;
import com.hy.haeyoback.global.exception.ErrorCode;
import com.hy.haeyoback.global.security.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Auth", description = "인증 관련 API")
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

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    @PostMapping("/signup")
    public ApiResponse<Void> signup(@Valid @RequestBody SignupRequest request) {
        userService.signup(request.getUsername(), request.getEmail(), request.getPassword());
        return ApiResponse.successMessage("Signup successful");
    }

    @Operation(summary = "로그인", description = "아이디와 비밀번호로 로그인하고 토큰을 발급받습니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/login")
    public ApiResponse<Void> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        AuthTokens tokens = authService.login(request.getUsername(), request.getPassword());
        setRefreshTokenCookie(response, tokens.getRefreshToken());
        response.setHeader("Authorization", "Bearer " + tokens.getAccessToken());
        return ApiResponse.successMessage("Login successful");
    }

    @Operation(summary = "토큰 재발급", description = "리프레시 토큰으로 새로운 액세스 토큰을 발급받습니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "유효하지 않은 리프레시 토큰")
    })
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

    @Operation(summary = "로그아웃", description = "서버에서 리프레시 토큰을 삭제하여 로그아웃 처리합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그아웃 성공")
    })
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
