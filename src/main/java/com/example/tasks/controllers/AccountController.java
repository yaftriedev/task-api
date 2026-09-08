package com.example.tasks.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tasks.config.JwtManager;
import com.example.tasks.dto.UserDTO;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/me")
public class AccountController {

    @GetMapping
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