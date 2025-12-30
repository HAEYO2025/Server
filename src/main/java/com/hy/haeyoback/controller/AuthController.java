package com.hy.haeyoback.controller;

import com.hy.haeyoback.dto.LoginRequest;
import com.hy.haeyoback.dto.SignupRequest;
import com.hy.haeyoback.security.JwtTokenProvider;
import com.hy.haeyoback.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")

public class AuthController {
    
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        try {
            // 1. 사용자 생성
            Map<String, Object> signupResult = userService.signup(request);

            // 2. username 추출
            String username = request.getUsername();

            // 3. JWT 생성
            String token = jwtTokenProvider.createToken(username);

            // 4. 응답 구성
            Map<String, Object> response = new HashMap<>();
            response.put("message", "회원가입 성공");
            response.put("token", token);
            response.put("username", username);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "회원가입에 실패했습니다");
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            Map<String, Object> response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/check")
    public ResponseEntity<?> checkAuth() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 🔐 인증 안 된 경우
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {

            Map<String, Object> response = new HashMap<>();
            response.put("authenticated", false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // ✅ 인증된 경우
        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", true);
        response.put("username", authentication.getName());

        return ResponseEntity.ok(response);
    }
}