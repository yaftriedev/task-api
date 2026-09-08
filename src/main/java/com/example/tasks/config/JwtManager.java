package com.example.tasks.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.tasks.dto.UserDTO;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtManager {

    private final static String SECRET = "una-clave-muy-larga-de-al-menos-32-caracteres";

    private final static SecretKey key = Keys.hmacShaKeyFor(
        SECRET.getBytes(StandardCharsets.UTF_8)
    );

    private static Claims claimToken(String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public static String createToken(UserDTO userDTO) {
        return Jwts.builder()
            .claim("id", userDTO.getId())
            .claim("name", userDTO.getName())
            .claim("email", userDTO.getEmail())
            .issuedAt(new Date())
            .expiration(new Date(
                System.currentTimeMillis() + 1000 * 60 * 60
            ))
            .signWith(key)
            .compact();
    }

    public static UserDTO getDataFromToken(String token) {

        Claims claims = claimToken(token);

        return new UserDTO(
            claims.get("id", Long.class),
            claims.get("name", String.class),
            claims.get("email", String.class)
        );
    }

    public static Long getIdFromToken(String token) {
        return claimToken(token).get("id", Long.class);
    }

    public static String getTokenFromHeader(String header) {
        
        if (header == null || !header.startsWith("Bearer ")) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "You need to send the JWT Token"
            );
        }

        return header.substring(7);
    }

}
