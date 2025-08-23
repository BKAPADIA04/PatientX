package com.systemdesign.auth_service.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.systemdesign.auth_service.dto.LoginRequestDTO;
import com.systemdesign.auth_service.util.JwtUtil;

import io.jsonwebtoken.JwtException;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Optional<String> authenticate(LoginRequestDTO loginRequest) {
        Optional<String> token = userService.findByEmail(loginRequest.getEmail())
                            .filter(u->passwordEncoder.matches(loginRequest.getPassword(), u.getPassword()))
                            .map(u->jwtUtil.generateToken(u.getEmail(), u.getRole()));
        return token;
    }

    public boolean validateToken(String token) {
        // Token validation logic can be added here if needed
        // For simplicity, we assume the token is valid if it can be parsed
        try {
            jwtUtil.validateToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
