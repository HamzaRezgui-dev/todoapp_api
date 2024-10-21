package com.todoapp.main.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todoapp.main.dto.UserLogin;
import com.todoapp.main.dto.UserRegistration;
import com.todoapp.main.service.UserService;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

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
    
}
