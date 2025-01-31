package com.paymybuddy.demo.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

@Service
public class JWTService {

    private static final Logger logger = LoggerFactory.getLogger(JWTService.class);
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public JWTService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    //  Générer un token
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();

        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .reduce((a, b) -> a + "," + b)
                .orElse("");

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("paymybuddy")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(authentication.getName())
                .claim("roles", roles)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();
    }

    // Valider un token
    public boolean validateToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            return jwt.getExpiresAt().isAfter(Instant.now());

        } catch (Exception e) {
            logger.error("Invalid token: {}", e.getMessage());
            return false;
        }
    }

    //  Extraire l'username du token
    public String extractUsername(String token) {
        return jwtDecoder.decode(token).getSubject();
    }

    //  Extraire les rôles du token
    public List<String> extractRoles(String token) {
        String roles = jwtDecoder.decode(token).getClaim("roles");
        return roles != null ? Arrays.asList(roles.split(",")) : Collections.emptyList();
    }
}


