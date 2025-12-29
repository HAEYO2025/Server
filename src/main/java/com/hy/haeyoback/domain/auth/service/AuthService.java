package com.hy.haeyoback.domain.auth.service;

import com.hy.haeyoback.domain.auth.dto.LoginResponse;
import com.hy.haeyoback.domain.user.entity.User;
import com.hy.haeyoback.domain.user.repository.UserRepository;
import com.hy.haeyoback.global.exception.CustomException;
import com.hy.haeyoback.global.exception.ErrorCode;
import com.hy.haeyoback.global.security.JwtProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public LoginResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "Invalid credentials");
        }

        String token = jwtProvider.generateToken(user.getId(), user.getEmail());
        return new LoginResponse(token);
    }
}
