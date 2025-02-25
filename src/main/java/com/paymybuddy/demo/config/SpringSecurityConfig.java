package com.paymybuddy.demo.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.paymybuddy.demo.filter.JWTFilter;
import com.paymybuddy.demo.service.JWTService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.spec.SecretKeySpec;

@Configuration
public class SpringSecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger( SpringSecurityConfig.class);

    @Value("${jwt.secret-key}")
    private String jwtKey;

    /**
     * Declares the JWT filter as a Bean.
     *
     * @param jwtService       Service for handling JWT operations.
     * @param userDetailsService Service for retrieving user-related data.
     * @return Configured JWTFilter instance.
     */
    @Bean

    public JWTFilter jwtFilter(JWTService jwtService, UserDetailsService userDetailsService) {
        return new JWTFilter(jwtService, userDetailsService);
    }
    /**
     * Configures the security filter chain.
     *
     * @param http         HttpSecurity configuration.
     * @param jwtFilter    JWT filter for authentication.
     * @return Configured SecurityFilterChain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(@NotNull HttpSecurity http, JWTFilter jwtFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/**").permitAll()
                        .requestMatchers("/login", "/register", "/profile", "/addConnection", "/transfer").permitAll()
                        .requestMatchers("/favicon.ico", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/api/connections", "/api/transfers").authenticated()
                        .requestMatchers("/api/transactions/**").authenticated()
                        .requestMatchers( "/api/**").authenticated())

                .logout(logout -> logout
                        .logoutSuccessUrl("/login") // Redirection après déconnexion
                        .permitAll()
                )

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // Ajoute le filtre JWT
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            logger.info("Authentication failed: " + authException.getMessage());
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        }))
                .build();
    }

    /**
     * Configures the JWT decoder.
     *
     * @return Configured JwtDecoder instance.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(new SecretKeySpec(jwtKey.getBytes(), "HmacSHA256")).build();
    }
    /**
     * Configures the JWT encoder.
     *
     * @return Configured JwtEncoder instance.
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtKey.getBytes()));
    }
    /**
     * Configures the password encoder using BCrypt.
     *
     * @return Configured BCryptPasswordEncoder instance.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /**
     * Configures the AuthenticationManager.
     *
     * @param authenticationConfiguration Configuration for authentication.
     * @return Configured AuthenticationManager instance.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    /**
     * Configures the authentication provider.
     *
     * @param userDetailsService Service for retrieving user-related data.
     * @param passwordEncoder    Encoder for passwords.
     * @return Configured DaoAuthenticationProvider instance.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
}
