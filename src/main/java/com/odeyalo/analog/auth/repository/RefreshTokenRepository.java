package com.odeyalo.analog.auth.repository;

import com.odeyalo.analog.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findRefreshTokenByRefreshToken(String id);

    void deleteRefreshTokenByUserId(Integer userId);
}
