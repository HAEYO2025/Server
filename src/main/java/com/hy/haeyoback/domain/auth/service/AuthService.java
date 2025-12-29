package com.hy.haeyoback.domain.auth.service;

import com.hy.haeyoback.domain.auth.dto.LoginResponse;
import com.hy.haeyoback.domain.user.entity.User;
import com.hy.haeyoback.domain.user.service.UserService;
import com.hy.haeyoback.global.security.JwtProvider;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    public AuthService(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    public LoginResponse login(String email, String password) {
        User user = userService.authenticate(email, password);

        String token = jwtProvider.generateToken(user.getId(), user.getEmail());
        return new LoginResponse(token);
    }
}
