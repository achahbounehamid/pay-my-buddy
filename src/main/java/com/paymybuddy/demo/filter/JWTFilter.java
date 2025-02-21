package com.paymybuddy.demo.filter;

import com.paymybuddy.demo.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class   JWTFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);
    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    public JWTFilter(JWTService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestPath = request.getServletPath();
        logger.info(" Requête interceptée : " + requestPath);

        // Liste des endpoints et fichiers statiques qui ne nécessitent pas d'authentification
        List<String> publicEndpoints = Arrays.asList(
                "/api/users/login", "/api/users/register",
                "/login", "/register", "/profile", "/addConnection", "/transfer",
                "/favicon.ico", "/css/", "/js/", "/images/"
        );

        // Vérifier si l'URL est publique (commence par un des préfixes définis)
        if (publicEndpoints.stream().anyMatch(requestPath::startsWith)) {
            logger.info("Endpoint public, pas de vérification du token : " + requestPath);
            filterChain.doFilter(request, response);
            return;
        }

        //  Ajout explicite de `/api/users/me` comme endpoint sécurisé nécessitant un token
        if (requestPath.startsWith("/api/users/me")) {
            logger.info(" Endpoint sécurisé : " + requestPath);
        }

        // Extraction du token JWT
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            logger.info("Token extrait : " + token);

            if (jwtService.validateToken(token)) {
                String username = jwtService.extractUsername(token);
                List<String> roles = jwtService.extractRoles(token);
                logger.info(" Token valide pour l'utilisateur : " + username + " avec rôles : " + roles);

                // Convertir les rôles en authorities
                Collection<GrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // Charger l'utilisateur et peupler le SecurityContext
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);

                logger.info(" Authentification réussie pour l'utilisateur : " + username);

            } else {
                logger.warn(" Token invalide ou expiré !");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalide ou expiré !");
                return;
            }
        } else {
            logger.warn(" Aucun token JWT fourni !");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Aucun token fourni !");
            return;
        }

        filterChain.doFilter(request, response);
    }
}






