package com.paymybuddy.demo.controller.rest;

import com.paymybuddy.demo.model.LoginRequest;
import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.paymybuddy.demo.service.JWTService;
import java.util.Collections;

@RestController
@RequestMapping("/api/users")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
            /**
            * Constructor to initialize services.
            *
            * @param jwtService           Service for handling JWT operations.
            * @param authenticationManager Manager for handling authentication.
            * @param userService          Service for user-related operations.
            */
    public LoginController(JWTService jwtService, AuthenticationManager authenticationManager, UserService userService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }
    /**
     * Endpoint to handle user login.
     *
     * @param loginRequest Request object containing user credentials.
     * @return ResponseEntity with JWT token or error message.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Login attempt for user: " + loginRequest.getEmail());

        // Check if the user exists by email
        User user = userService.findUserByEmail(loginRequest.getEmail());
        if (user == null) {
            logger.info(" Login failed: User not found with this email.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(), loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            //  Generate JWT token
            String token = jwtService.generateToken(authentication);
            logger.info("Token generated for user: " + loginRequest.getEmail());

            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (BadCredentialsException e) {
            logger.warn(" Login failed: Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
    /**
     * Endpoint to retrieve user information based on JWT token.
     *
     * @param request HttpServletRequest object containing the JWT token.
     * @return ResponseEntity with user information or error message.
     */
    @GetMapping("/me/info")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        //  Check for the presence of the JWT token
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Invalid or expired token"));
        }

        // Extract the username from the token
        token = token.substring(7);

        // Validate the token
        if (!jwtService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Token invalide ou expiré"));
        }

        // Extract the username from the token
        String username = jwtService.extractUsername(token);

        // Return the username
        return ResponseEntity.ok(Collections.singletonMap("username", username));
    }

}

