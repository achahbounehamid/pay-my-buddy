package com.paymybuddy.demo.controller;


import com.paymybuddy.demo.model.LoginRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.paymybuddy.demo.service.JWTService;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

@RestController
@RequestMapping("/api/users")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    public LoginController(JWTService jwtService, AuthenticationManager authenticationManager) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Tentative de connexion pour l'utilisateur: " + loginRequest.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(), loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Générer le token JWT
            String token = jwtService.generateToken(authentication);
            logger.info("Token généré pour l'utilisateur: " + loginRequest.getUsername());

            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (BadCredentialsException e) {
            logger.warn("Échec de connexion: Identifiants incorrects");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiants incorrects");
        }
    }

}

