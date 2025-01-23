//package com.paymybuddy.demo.security;
//
//import com.paymybuddy.demo.service.JwtService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        // Chercher le token dans l'en-tête "Authorization"
//        String token = request.getHeader("Authorization");
//
//        // Si le token est présent et commence par "Bearer "
//        if (token != null && token.startsWith("Bearer ")) {
//            token = token.substring(7);  // Extraire le token sans "Bearer "
//
//            // Vérifier si le token est valide
//            if (jwtService.isTokenValid(token)) {
//                // Extraire l'email ou d'autres informations utilisateur à partir du token
//                String email = jwtService.extractEmail(token);
//
//                // Créer une instance d'Authentication et l'ajouter au SecurityContext
//                User principal = new User(email, "", new ArrayList<>()); // Par exemple, sans mot de passe et avec un rôle vide
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
//                SecurityContextHolder.getContext().setAuthentication(authentication); // Mettre l'authentification dans le contexte de sécurité
//            }
//        }
//
//        // Continuer la chaîne de filtres
//        filterChain.doFilter(request, response);
//    }
//}
//
