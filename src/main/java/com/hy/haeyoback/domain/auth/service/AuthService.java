package com.hy.haeyoback.domain.auth.service;

import com.hy.haeyoback.domain.auth.dto.AuthTokens;
import com.hy.haeyoback.domain.auth.entity.RefreshToken;
import com.hy.haeyoback.domain.auth.repository.RefreshTokenRepository;
import com.hy.haeyoback.domain.user.entity.User;
import com.hy.haeyoback.domain.user.service.UserService;
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

    public AuthTokens login(String username, String password) {
        User user = userService.authenticate(username, password);

        String accessToken = jwtProvider.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId());
        JwtProvider.RefreshTokenInfo info = jwtProvider.parseRefreshToken(refreshToken);
        saveRefreshToken(user.getId(), refreshToken, info.expiresAt());
        return new AuthTokens(accessToken, refreshToken);
    }

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

    public void logout(String refreshToken) {
        refreshTokenRepository.deleteByToken(refreshToken);
    }

    @Transactional
    private void saveRefreshToken(Long userId, String refreshToken, Instant expiresAt) {
        RefreshToken token = refreshTokenRepository.findByUserIdForUpdate(userId)
                .orElse(null);
        if (token == null) {
            try {
                refreshTokenRepository.save(new RefreshToken(userId, refreshToken, expiresAt));
                return;
            } catch (DataIntegrityViolationException ex) {
                token = refreshTokenRepository.findByUserIdForUpdate(userId)
                        .orElseThrow(() -> ex);
            }
        }
        token.rotate(refreshToken, expiresAt);
        refreshTokenRepository.save(token);
    }
}
