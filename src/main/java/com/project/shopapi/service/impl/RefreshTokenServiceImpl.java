package com.project.shopapi.service.impl;

import com.project.shopapi.entity.RefreshToken;
import com.project.shopapi.entity.User;
import com.project.shopapi.exception.ErrorVerifiedRefreshToken;
import com.project.shopapi.exception.NotFoundRefreshToken;
import com.project.shopapi.repository.RefreshTokenRepository;
import com.project.shopapi.repository.UserRepository;
import com.project.shopapi.service.inf.RefreshTokenService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    //    @Value("${app.jwtExpirationRf}")
    private final int jwtExpirationRf;

    public RefreshTokenServiceImpl(@Value("${app.jwtExpirationRf}") int jwtExpirationRf) {
        this.jwtExpirationRf = jwtExpirationRf;
    }

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(jwtExpirationRf));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public RefreshToken refreshToken(User user) {
        RefreshToken refreshToken = user.getRefreshToken();
        refreshToken.setExpiryDate(Instant.now().plusMillis(jwtExpirationRf));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }



    @Override
    public boolean verifyExpiration(RefreshToken refreshToken) {

        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new ErrorVerifiedRefreshToken("Refresh token was expired. Please make a new signin request");
        } else {
            return true;
        }
//        if (!refreshTokenRepository.findByUser(user).isPresent()) {
//            createRefreshToken(user.getId()) ;
//        } else if (refreshTokenRepository.findByUser(user).get().getExpiryDate().compareTo(Instant.now()) < 0) {
//            refreshTokenRepository.delete(refreshTokenRepository.findByUser(user).get());
//            throw new ErrorVerifiedRefreshToken("Refresh token was expired. Please make a new signin request");
//        } else {
//            return refreshTokenRepository.findByUser(user).get();
//        }
//        return refreshTokenRepository.findByUser(user).get();
    }
}
