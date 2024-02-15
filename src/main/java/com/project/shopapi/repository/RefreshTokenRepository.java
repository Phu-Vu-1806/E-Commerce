package com.project.shopapi.repository;

import com.project.shopapi.entity.RefreshToken;
import com.project.shopapi.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
    boolean existsByToken(String refreshToken);

    void deleteByUser(User user);
}
