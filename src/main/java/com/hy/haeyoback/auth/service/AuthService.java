package com.hy.haeyoback.auth.service;

import com.hy.haeyoback.auth.dto.AuthTokens;
import com.hy.haeyoback.auth.entity.RefreshToken;
import com.hy.haeyoback.auth.repository.RefreshTokenRepository;
import com.hy.haeyoback.user.entity.User;
import com.hy.haeyoback.user.service.UserService;
import com.hy.haeyoback.global.exception.CustomException;
import com.hy.haeyoback.global.exception.ErrorCode;
import com.hy.haeyoback.global.security.JwtProvider;
import java.time.Instant;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(
            UserService userService,
            JwtProvider jwtProvider,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public AuthTokens login(String email, String password) {
        User user = userService.authenticate(email, password);

        String accessToken = jwtProvider.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId());
        JwtProvider.RefreshTokenInfo info = jwtProvider.parseRefreshToken(refreshToken);
        saveRefreshToken(user.getId(), refreshToken, info.expiresAt());
        return new AuthTokens(accessToken, refreshToken);
    }

    @Transactional
    public AuthTokens refresh(String refreshToken) {
        JwtProvider.RefreshTokenInfo info = jwtProvider.parseRefreshToken(refreshToken);
        Long userId = info.userId();
        RefreshToken stored = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED, "Invalid refresh token"));
        if (!stored.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "Invalid refresh token");
        }
        User user = userService.getUserEntity(userId);
        String newAccessToken = jwtProvider.generateAccessToken(user.getId(), user.getEmail());
        String newRefreshToken = jwtProvider.generateRefreshToken(user.getId());
        JwtProvider.RefreshTokenInfo newInfo = jwtProvider.parseRefreshToken(newRefreshToken);
        saveRefreshToken(user.getId(), newRefreshToken, newInfo.expiresAt());
        return new AuthTokens(newAccessToken, newRefreshToken);
    }

    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }

    private void saveRefreshToken(Long userId, String refreshToken, Instant expiresAt) {
        RefreshToken token = refreshTokenRepository.findByUserId(userId)
                .orElse(null);
        if (token == null) {
            try {
                refreshTokenRepository.save(new RefreshToken(userId, refreshToken, expiresAt));
                return;
            } catch (DataIntegrityViolationException ex) {
                token = refreshTokenRepository.findByUserId(userId)
                        .orElseThrow(() -> ex);
            }
        }
        token.rotate(refreshToken, expiresAt);
        refreshTokenRepository.save(token);
    }
}
