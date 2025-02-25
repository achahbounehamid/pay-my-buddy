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
    /**
     * Constructor to initialize services.
     *
     * @param jwtService         Service for handling JWT operations.
     * @param userDetailsService Service for retrieving user-related data.
     */
    public JWTFilter(JWTService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestPath = request.getServletPath();
        logger.info(" Request intercepted: " + requestPath);

        // List of public endpoints and static files that do not require authentication
        List<String> publicEndpoints = Arrays.asList(
                "/api/users/login", "/api/users/register",
                "/login", "/register", "/profile", "/addConnection", "/transfer",
                "/favicon.ico", "/css/", "/js/", "/images/"
        );

        // Check if the URL is public (starts with one of the defined prefixes)
        if (publicEndpoints.stream().anyMatch(requestPath::startsWith)) {
            logger.info("Public endpoint, no token verification needed: " + requestPath);
            filterChain.doFilter(request, response);
            return;
        }

        // Explicitly add `/api/users/me` as a secured endpoint requiring a token
        if (requestPath.startsWith("/api/users/me")) {
            logger.info(" Secured endpoint : " + requestPath);
        }

        // Extract the JWT token
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            logger.info("Token extracted : " + token);

            if (jwtService.validateToken(token)) {
                String username = jwtService.extractUsername(token);
                List<String> roles = jwtService.extractRoles(token);
                logger.info(" Valid token for user : " + username + " with roles : " + roles);

                // Convert roles to authorities
                Collection<GrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // Load the user and populate the SecurityContext
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);

                logger.info(" Authentication successful for user: " + username);

            } else {
                logger.warn(" Invalid or expired token!");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token!");
                return;
            }
        } else {
            logger.warn(" No JWT token provided!");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No token provided!");
            return;
        }

        filterChain.doFilter(request, response);
    }
}






