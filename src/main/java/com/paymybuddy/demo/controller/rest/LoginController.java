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

    public LoginController(JWTService jwtService, AuthenticationManager authenticationManager, UserService userService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        logger.info("Tentative de connexion pour l'utilisateur: " + loginRequest.getEmail());

        // Vérifier si l'utilisateur existe avec son email
        User user = userService.findUserByEmail(loginRequest.getEmail());
        if (user == null) {
            logger.info(" Échec de connexion: Utilisateur non trouvé avec cet email.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiants incorrects");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(), loginRequest.getPassword() //  Utiliser username
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            //  Générer le token JWT
            String token = jwtService.generateToken(authentication);
            logger.info("Token généré pour l'utilisateur: " + loginRequest.getEmail());

            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (BadCredentialsException e) {
            logger.warn(" Échec de connexion: Identifiants incorrects");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiants incorrects");
        }
    }

    @GetMapping("/me/info")
    public ResponseEntity<?> getUserInfo(HttpServletRequest request) {
        // Vérifier la présence du token JWT
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Token manquant ou invalide"));
        }

        // Extraction du token sans "Bearer "
        token = token.substring(7);

        // Vérifier la validité du token
        if (!jwtService.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("error", "Token invalide ou expiré"));
        }

        // Extraire le username à partir du token
        String username = jwtService.extractUsername(token);

        // Retourner le nom d'utilisateur
        return ResponseEntity.ok(Collections.singletonMap("username", username));
    }

}

