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

public class JWTFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);
    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    public JWTFilter(JWTService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        String path = request.getServletPath();
//        logger.info("Checking if path should be filtered: " + path);
//        return path.equals("/register") || path.equals("/login");
//    }

    @Override
    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestPath = request.getServletPath();
        List<String> publicEndpoints = Arrays.asList("/api/users/login", "/api/users/register");

        if (publicEndpoints.contains(requestPath)) {
            logger.info("Skipping JWT filter for path: {}", requestPath);
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                if (jwtService.validateToken(token)) {
                    String username = jwtService.extractUsername(token);
                    List<String> roles = jwtService.extractRoles(token);

                    logger.info("Token valid for user: {} with roles: {}", username, roles);

                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // Convertir les rôles en SimpleGrantedAuthority
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    logger.warn("Token invalide ou expiré !");
                }
            } catch (Exception e) {
                logger.error("Erreur lors de la validation du token: ", e);
            }
        } else {
            logger.info("Aucun token trouvé pour {}", request.getServletPath());
        }

        filterChain.doFilter(request, response);
    }
}




