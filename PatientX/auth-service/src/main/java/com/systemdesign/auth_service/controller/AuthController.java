package com.systemdesign.auth_service.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.systemdesign.auth_service.dto.LoginRequestDTO;
import com.systemdesign.auth_service.dto.LoginResponseDTO;
import com.systemdesign.auth_service.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Generate token on user login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        // Logic to authenticate user and generate token
        Optional <String> tokenOptional = authService.authenticate(loginRequest);
        if (tokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {
            String token = tokenOptional.get();
            return ResponseEntity.ok(new LoginResponseDTO(token));
        }
    }
}
