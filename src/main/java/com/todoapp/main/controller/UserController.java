package com.todoapp.main.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todoapp.main.dto.JwtRefreshRequestDto;
import com.todoapp.main.dto.JwtResponseDto;
import com.todoapp.main.dto.UserLogin;
import com.todoapp.main.dto.UserRegistration;
import com.todoapp.main.service.RefreshTokenService;
import com.todoapp.main.service.UserService;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> Register(@RequestBody UserRegistration userRegistration) {
        Map<String, String> tokenResponse = userService.registerUser(userRegistration);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> Login(@RequestBody UserLogin userLogin) {
        Map<String, String> tokenResponse = userService.loginUser(userLogin);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDto> Refresh(@RequestBody JwtRefreshRequestDto jwtRefreshRequestDto) {
        JwtResponseDto tokenResponse = refreshTokenService.refreshToken(jwtRefreshRequestDto);
        return ResponseEntity.ok(tokenResponse);
    }
    
}
