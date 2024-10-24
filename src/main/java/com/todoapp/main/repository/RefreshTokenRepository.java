package com.todoapp.main.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todoapp.main.entity.RefreshToken;
import com.todoapp.main.entity.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
   Optional<RefreshToken> findRefreshTokenByToken(String token);
   RefreshToken findByUser(User user);
}
