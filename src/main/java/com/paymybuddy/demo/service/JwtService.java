package com.paymybuddy.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final String secretKey = "secret"; // Vous pouvez la rendre plus complexe

    // Vérifie si le token est valide
    public boolean isTokenValid(String token) {
        try {
            extractClaims(token); // Si l'extraction des claims réussit, le token est valide
            return true;
        } catch (Exception e) {
            return false; // Si une exception est lancée, le token est invalide
        }
    }

    // Extraire les informations des claims
    private Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    // Extraire l'email de l'utilisateur à partir du token
    public String extractEmail(String token) {
        return extractClaims(token).getSubject(); // Subject est souvent l'email de l'utilisateur
    }

    // Méthode pour générer un token (à utiliser lors de la connexion)
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
