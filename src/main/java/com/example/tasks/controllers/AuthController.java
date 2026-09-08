package com.example.tasks.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.tasks.config.JwtManager;
import com.example.tasks.dto.UserDTO;
import com.example.tasks.dto.UserLoginDTO;
import com.example.tasks.dto.UserRegisterDTO;
import com.example.tasks.services.AuthService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO userLoginDTO) {
        return ResponseEntity.ok(
            authService.login(userLoginDTO)
        );
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        return ResponseEntity.ok(
            authService.register(userRegisterDTO)
        );
    }    
    
    @GetMapping("/data")
    public ResponseEntity<UserDTO> getAccountnfo(
        HttpServletRequest request
    ) {
        return ResponseEntity.ok(
            JwtManager.getDataFromToken( 
                JwtManager.getTokenFromHeader(
                    request.getHeader("Authorization")
                ) 
            )
        );
    }
}
