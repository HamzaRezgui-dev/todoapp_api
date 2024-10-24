package com.todoapp.main.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.todoapp.main.dto.JwtRefreshRequestDto;
import com.todoapp.main.dto.JwtResponseDto;
import com.todoapp.main.entity.RefreshToken;
import com.todoapp.main.entity.User;
import com.todoapp.main.repository.RefreshTokenRepository;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JWTService jwtService;
    

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, JWTService jwtService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }

    public String createToken(User user) {
        RefreshToken existingToken = refreshTokenRepository.findByUser(user);
    
        if (existingToken != null) {
            // Delete the existing refresh token
            refreshTokenRepository.delete(existingToken);
        }
        var refreshToken = refreshTokenRepository.save(RefreshToken.builder()
        .token(UUID.randomUUID().toString())
        .user(user)
        .expiration(ZonedDateTime.now(ZoneId.systemDefault()).plusDays(1))
        .build());
        return refreshToken.getToken();
    }

    public JwtResponseDto refreshToken(JwtRefreshRequestDto refreshRequestDto) {
        var TokenOpt = refreshTokenRepository.findRefreshTokenByToken(refreshRequestDto.getRefreshToken());
        if (TokenOpt.isEmpty()) {
            throw new RuntimeException("Refresh token %s not found!".formatted(refreshRequestDto.getRefreshToken()));
        }
        var token = TokenOpt.get();
        if (isTokenExpired(token.getExpiration())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token %s has expired!".formatted(refreshRequestDto.getRefreshToken()));
        }
        String jwt = jwtService.generateJwt(token.getUser().getEmail());
        updateToken(token);

        return JwtResponseDto.of(jwt, token.getToken());
    }

    private void updateToken(RefreshToken token) {
        token.setExpiration(ZonedDateTime.now(ZoneId.systemDefault()));
        refreshTokenRepository.save(token);
    }
    
    private boolean isTokenExpired(ZonedDateTime expirationTime) {
        return expirationTime.isBefore(ZonedDateTime.now(ZoneId.systemDefault()));
    }

}
