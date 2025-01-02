package com.example.bloggingbackend.controller;

import com.example.bloggingbackend.model.User;
import com.example.bloggingbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Endpoint for signup
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        try {
            return new ResponseEntity<>(authService.register(user), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("User registration failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint for login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try {
            String jwtToken = authService.login(user.getUsername(), user.getPassword());
            return new ResponseEntity<>(jwtToken, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Login failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
