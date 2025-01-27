//package com.paymybuddy.demo.security;
//
//import com.paymybuddy.demo.service.JWTService;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//public class JWTAuthenticationFilter extends OncePerRequestFilter {
//
//    private final JWTService jwtService;
//
//    public JWTAuthenticationFilter(JWTService jwtService) {
//        this.jwtService = jwtService;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        // Extraction du token depuis l'en-tête Authorization
//        String token = extractToken(request);
//        if (token != null && jwtService.isTokenValid(token)) {
//            // Validation du token et récupération des informations d'authentification
//            Authentication authentication = jwtService.getAuthentication(token);
//            SecurityContextHolder.getContext().setAuthentication(authentication); // Ajout au contexte de sécurité
//        }
//
//        // Continuer la chaîne de filtres
//        filterChain.doFilter(request, response);
//    }
//
//    /**
//     * Extrait le token depuis l'en-tête Authorization
//     */
//    private String extractToken(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7); // Supprime le préfixe "Bearer "
//        }
//        return null;
//    }
//}
//
