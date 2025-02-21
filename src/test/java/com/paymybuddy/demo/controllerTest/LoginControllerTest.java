package com.paymybuddy.demo.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymybuddy.demo.controller.rest.LoginController;
import com.paymybuddy.demo.filter.JWTFilter;
import com.paymybuddy.demo.model.LoginRequest;
import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.service.JWTService;
import com.paymybuddy.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(LoginControllerTest.SecurityOverrideConfig.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserService userService;

    @MockBean
    private JWTFilter jwtFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void login_ShouldReturnToken_WhenCredentialsAreValid() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("adam@gmail.com");
        loginRequest.setPassword("adam1234");

        User user = new User();
        user.setEmail("adam@gmail.com");
        user.setUsername("Adam");

        Authentication authentication = mock(Authentication.class);

        when(userService.findUserByEmail("adam@gmail.com")).thenReturn(user);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtService.generateToken(authentication)).thenReturn("mockedJwtToken");

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockedJwtToken"));
    }

    @Test
    @WithMockUser
    void login_ShouldReturnUnauthorized_WhenUserNotFound() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("unknown@example.com");
        loginRequest.setPassword("password123");

        when(userService.findUserByEmail("unknown@example.com")).thenReturn(null);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Identifiants incorrects"));
    }

    @Test
    @WithMockUser
    void login_ShouldReturnUnauthorized_WhenBadCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("adam@gmail.com");
        loginRequest.setPassword("wrongpassword");

        User user = new User();
        user.setEmail("adam@gmail.com");

        when(userService.findUserByEmail("adam@gmail.com")).thenReturn(user);
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Identifiants incorrects"));
    }

    @TestConfiguration
    static class SecurityOverrideConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }
}
