package com.paymybuddy.demo.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class JWTService {


    private JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public JWTService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generateToken(Authentication authentication) {
        System.out.println("Generating token for user: " + authentication.getName());
            Instant now = Instant.now();

        // Extraire les rôles (authorities) de l'utilisateur
        String roles = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .reduce((a, b) -> a + "," + b) // Concaténer les rôles avec une virgule
                .orElse(""); // Si aucun rôle, chaîne vide
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(now)
                    .expiresAt(now.plus(1, ChronoUnit.DAYS))
                    .subject(authentication.getName())
                    .claim("roles", roles)
                    .build();
            JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
            return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }
    // Extraire le "subject" (par exemple, l'email) du token
    public String extractUsername(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getClaim("sub"); // Récupérer le champ "subject"
    }

    // Extraire les rôles du token
    public List<String> extractRoles(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        String roles = jwt.getClaim("roles");
        return roles != null ? Arrays.asList(roles.split(",")) : Collections.emptyList();
    }

    // Valider si le token est valide ou expiré
    public boolean validateToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            Instant expiration = jwt.getExpiresAt();
            return expiration != null && expiration.isAfter(Instant.now());
        } catch (JwtException e) {
            return false;
        }
    }

}
