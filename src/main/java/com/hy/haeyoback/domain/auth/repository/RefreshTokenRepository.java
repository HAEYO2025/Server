package com.hy.haeyoback.domain.auth.repository;

import com.hy.haeyoback.domain.auth.entity.RefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.LockModeType;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<RefreshToken> findByUserId(Long userId);

    void deleteByUserId(Long userId);

    void deleteByToken(String token);
}
