package com.example.demo.Service.auth;

import com.example.demo.Repository.user.UserRepo;
import com.example.demo.Repository.auth.RefreshTokenRepo;
import com.example.demo.entity.User;
import com.example.demo.entity.auth.RefreshToken;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.exceptions.auth.TokenRefreshException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Data
public class RefreshTokenService {
    @Value("${app.jwt.expire}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepo refreshTokenRepo;


    private final UserRepo userRepo;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepo.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
        User user = userRepo.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId)
        );
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepo.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepo.delete(token);
            throw new TokenRefreshException(
                    token.getToken(),
                    "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        return refreshTokenRepo.deleteByUser(userRepo.findById(userId).get());
    }
}