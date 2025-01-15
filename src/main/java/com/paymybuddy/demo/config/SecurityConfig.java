package com.paymybuddy.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers("/api/users/**").permitAll()  // Permet l'accès sans authentification
                .anyRequest().authenticated()  // Exige l'authentification pour les autres requêtes
                .and()
                .csrf().disable();  // Désactive la protection CSRF si nécessaire

        return http.build();
    }
}
