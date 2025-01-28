package com.paymybuddy.demo.controller;


import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.paymybuddy.demo.service.JWTService;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class LoginController {


    private JWTService jwtService;

    public LoginController(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public String getToken(Authentication authentication) {
        System.out.println("Login endpoint called");
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication failed");
        }
        // Génération du token si l'authentification est réussie
        System.out.println("Authentication successful for user: " + authentication.getName());
        return jwtService.generateToken(authentication);
    }

}

