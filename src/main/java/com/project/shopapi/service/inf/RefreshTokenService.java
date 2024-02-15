package com.project.shopapi.service.inf;

import com.project.shopapi.entity.RefreshToken;
import com.project.shopapi.entity.User;

import java.util.Optional;

public interface RefreshTokenService {

    Optional<RefreshToken> findByToken(String token);

    RefreshToken createRefreshToken(User user);

    boolean verifyExpiration(RefreshToken refreshToken);

    RefreshToken refreshToken(User user);

}
