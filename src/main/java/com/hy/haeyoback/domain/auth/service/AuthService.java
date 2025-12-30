package com.hy.haeyoback.domain.auth.service;

import com.hy.haeyoback.domain.auth.dto.AuthTokens;
import com.hy.haeyoback.domain.auth.entity.RefreshToken;
import com.hy.haeyoback.domain.auth.repository.RefreshTokenRepository;
import com.hy.haeyoback.domain.user.entity.User;
import com.hy.haeyoback.domain.user.service.UserService;
import com.hy.haeyoback.global.exception.CustomException;
import com.hy.haeyoback.global.exception.ErrorCode;
import com.hy.haeyoback.global.security.JwtProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import java.time.Instant;
import org.springframework.stereotype.Service;

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

    public AuthTokens login(String email, String password) {
        User user = userService.authenticate(email, password);

        String accessToken = jwtProvider.generateAccessToken(user.getId(), user.getEmail());
        String refreshToken = jwtProvider.generateRefreshToken(user.getId());
        JwtProvider.RefreshTokenInfo info = jwtProvider.parseRefreshToken(refreshToken);
        saveRefreshToken(user.getId(), refreshToken, info.expiresAt());
        return new AuthTokens(accessToken, refreshToken);
    }

    public AuthTokens refresh(String refreshToken) {
        JwtProvider.RefreshTokenInfo info;
        try {
            info = jwtProvider.parseRefreshToken(refreshToken);
        } catch (ExpiredJwtException ex) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "Token expired");
        } catch (JwtException | IllegalArgumentException ex) {
            throw new CustomException(ErrorCode.UNAUTHORIZED, "Invalid token");
        }
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

    private void saveRefreshToken(Long userId, String refreshToken, Instant expiresAt) {
        RefreshToken token = refreshTokenRepository.findByUserId(userId)
                .orElse(new RefreshToken(userId, refreshToken, Instant.EPOCH));
        token.rotate(refreshToken, expiresAt);
        refreshTokenRepository.save(token);
    }
}
