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
/**
 * Service class for managing JWT operations.
 * This class provides methods to generate, validate, and extract information from JWT tokens.
 */
@Service
public class JWTService {

    private static final Logger logger = LoggerFactory.getLogger(JWTService.class);
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    /**
     * Constructor to initialize JWT encoder and decoder.
     *
     * @param jwtEncoder The encoder for creating JWT tokens.
     * @param jwtDecoder The decoder for parsing JWT tokens.
     */
    public JWTService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    /**
     * Generates a JWT token for a given authentication.
     *
     * @param authentication The authentication object containing user details.
     * @return A JWT token as a string.
     */
    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
// Extract roles from the authentication object
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .reduce((a, b) -> a + "," + b)
                .orElse("");
// Build the JWT claims set
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("paymybuddy")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(authentication.getName())
                .claim("roles", roles)
                .build();
// Encode the JWT token
        return jwtEncoder.encode(JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();
    }

    /**
     * Validates a JWT token.
     *
     * @param token The JWT token to validate.
     * @return True if the token is valid, otherwise false.
     */
    public boolean validateToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            Instant expiration = jwt.getExpiresAt();
            logger.info(" Token expiration: " + expiration);

            if (expiration == null) {
                logger.warn("Token has no expiration date!");
                return false;
            }

            boolean isValid = expiration.isAfter(Instant.now());
            logger.info(" Token valid : " + isValid);
            return isValid;
        } catch (Exception e) {
            logger.error("Error during token validation:" + e.getMessage());
            return false;
        }
    }

    /**
     * Extracts the username from a JWT token.
     *
     * @param token The JWT token.
     * @return The username extracted from the token.
     */
    public String extractUsername(String token) {
        return jwtDecoder.decode(token).getSubject();
    }

    /**
     * Extracts the roles from a JWT token.
     *
     * @param token The JWT token.
     * @return A list of roles extracted from the token.
     */
    public List<String> extractRoles(String token) {
        String roles = jwtDecoder.decode(token).getClaim("roles");
        return roles != null ? Arrays.asList(roles.split(",")) : Collections.emptyList();
    }
}


