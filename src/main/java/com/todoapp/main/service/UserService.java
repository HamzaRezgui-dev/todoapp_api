package com.todoapp.main.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.todoapp.main.dto.UserLogin;
import com.todoapp.main.dto.UserRegistration;
import com.todoapp.main.entity.User;
import com.todoapp.main.exception.IncorrectCredentialsException;
import com.todoapp.main.exception.UserAlreadyExistsException;
import com.todoapp.main.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JWTService jwtService;

    public Map<String, String> registerUser(UserRegistration request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }
        try {
            String hashedPassword = passwordEncoder.encode(request.getPassword());
            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setUsername(request.getUsername());
            user.setPassword(hashedPassword);
            userRepository.save(user);

            Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            Map<String, String> tokenResponse = new HashMap<>();
            String token = jwtService.generateJwt(auth);
            tokenResponse.put("token", token);
            return tokenResponse;
        } catch (Exception e) {
            throw new RuntimeException("Invalid credentials");
        }
    }

    public Map<String, String> loginUser(UserLogin request) {
        try {
            Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            Map<String, String> tokenResponse = new HashMap<>();
            String token = jwtService.generateJwt(auth);
            tokenResponse.put("token", token);
            return tokenResponse;
        } catch (Exception e) {
            throw new IncorrectCredentialsException("Incorrect email or password");
        }
    }
    
}
