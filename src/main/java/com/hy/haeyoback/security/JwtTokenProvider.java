//package com.hy.haeyoback.security;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import javax.crypto.SecretKey;
//import java.util.Date;
//
//@Component
//public class JwtTokenProvider {
//
//    private final SecretKey secretKey;
//    private final long expirationTime;
//
//    public JwtTokenProvider(
//            @Value("${jwt.secret}") String secret,
//            @Value("${jwt.expiration}") long expirationTime
//    ) {
//        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
//        this.expirationTime = expirationTime;
//    }
//
//    // ✅ 1. 토큰 생성
//    public String createToken(String username) {
//        Date now = new Date();
//        Date expiry = new Date(now.getTime() + expirationTime);
//
//        return Jwts.builder()
//                .subject(username)
//                .issuedAt(now)
//                .expiration(expiry)
//                .signWith(secretKey, Jwts.SIG.HS256)
//                .compact();
//    }
//
//    // ✅ 2. 토큰에서 username 추출
//    public String getUsername(String token) {
//        return Jwts.parser()
//                .verifyWith(secretKey)
//                .build()
//                .parseSignedClaims(token)
//                .getPayload()
//                .getSubject();
//    }
//
//    // ✅ 3. 토큰 유효성 검증
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parser()
//                    .verifyWith(secretKey)
//                    .build()
//                    .parse(token);
//            return true;
//        } catch (JwtException | IllegalArgumentException e) {
//            return false;
//        }
//    }
//}
