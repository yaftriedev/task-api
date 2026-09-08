package com.example.tasks.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class WelcomeController {
    
    @GetMapping
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok("welcome");
    }

}
