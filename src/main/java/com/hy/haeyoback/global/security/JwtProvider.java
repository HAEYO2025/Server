package com.hy.haeyoback.global.security;

import com.hy.haeyoback.global.constants.JwtConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.nio.charset.StandardCharsets;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private final JwtProperties properties;
    private final Key signingKey;

    public JwtProvider(JwtProperties properties) {
        this.properties = properties;
        this.signingKey = Keys.hmacShaKeyFor(properties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Long userId, String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + properties.getExpireTime());
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim(JwtConstants.CLAIM_EMAIL, email)
                .claim(JwtConstants.CLAIM_TOKEN_TYPE, JwtConstants.TOKEN_TYPE_ACCESS)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + properties.getRefreshExpireTime());
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim(JwtConstants.CLAIM_TOKEN_TYPE, JwtConstants.TOKEN_TYPE_REFRESH)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        ensureAccessToken(claims);
        String subject = claims.getSubject();
        String email = claims.get(JwtConstants.CLAIM_EMAIL, String.class);
        Long userId;
        try {
            userId = Long.valueOf(subject);
        } catch (NumberFormatException ex) {
            throw new JwtException(JwtConstants.ERROR_INVALID_SUBJECT, ex);
        }
        JwtUserDetails principal = new JwtUserDetails(userId, email);
        return new UsernamePasswordAuthenticationToken(
                principal,
                null,
                java.util.List.of(new SimpleGrantedAuthority(JwtConstants.ROLE_USER))
        );
    }

    public void validateAccessToken(String token) {
        Claims claims = parseClaims(token);
        ensureAccessToken(claims);
    }

    public Long validateRefreshToken(String token) {
        Claims claims = parseClaims(token);
        ensureRefreshToken(claims);
        String subject = claims.getSubject();
        try {
            return Long.valueOf(subject);
        } catch (NumberFormatException ex) {
            throw new JwtException(JwtConstants.ERROR_INVALID_SUBJECT, ex);
        }
    }

    public RefreshTokenInfo parseRefreshToken(String token) {
        Claims claims = parseClaims(token);
        ensureRefreshToken(claims);
        String subject = claims.getSubject();
        Long userId;
        try {
            userId = Long.valueOf(subject);
        } catch (NumberFormatException ex) {
            throw new JwtException(JwtConstants.ERROR_INVALID_SUBJECT, ex);
        }
        return new RefreshTokenInfo(userId, claims.getExpiration().toInstant());
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private void ensureAccessToken(Claims claims) {
        String type = claims.get(JwtConstants.CLAIM_TOKEN_TYPE, String.class);
        if (!JwtConstants.TOKEN_TYPE_ACCESS.equals(type)) {
            throw new JwtException(JwtConstants.ERROR_INVALID_TOKEN_TYPE);
        }
    }

    private void ensureRefreshToken(Claims claims) {
        String type = claims.get(JwtConstants.CLAIM_TOKEN_TYPE, String.class);
        if (!JwtConstants.TOKEN_TYPE_REFRESH.equals(type)) {
            throw new JwtException(JwtConstants.ERROR_INVALID_TOKEN_TYPE);
        }
    }

    public record RefreshTokenInfo(Long userId, Instant expiresAt) {
    }
}
