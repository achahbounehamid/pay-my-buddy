package com.paymybuddy.demo.filter;

import com.paymybuddy.demo.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
@Component
public class JWTFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);
    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    public JWTFilter(JWTService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        logger.info("Checking if path should be filtered: " + path);
        return path.equals("/register") || path.equals("/login");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestPath = request.getServletPath();

        // 🔹 Liste des endpoints publics à exclure du filtrage JWT
        List<String> publicEndpoints = Arrays.asList("/api/users/login", "/api/users/register", "/api/users");

        if (publicEndpoints.contains(requestPath)) {
            logger.info("Skipping JWT filter for path: {}", requestPath);
            filterChain.doFilter(request, response);
            return;
        }

        // Récupérer le header Authorization
        String authHeader = request.getHeader("Authorization");

        logger.info("Authorization Header: {}", authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extraire le token après "Bearer "
            String token = authHeader.substring(7);
            logger.info("Extracted Token: {}", token);

            if (jwtService.validateToken(token)) {
                // Extraire les informations du token
                String username = jwtService.extractUsername(token);
                List<String> roles = jwtService.extractRoles(token);

                logger.info("Token valid for user: {} with roles: {}", username, roles);

                // Charger l'utilisateur à partir du UserDetailsService
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                logger.info("Invalid Token: {}", token);
            }
        } else {
            logger.info("No Authorization header or token provided for path: {}", request.getServletPath());
        }

        // Passer au filtre suivant
        filterChain.doFilter(request, response);
    }
}




