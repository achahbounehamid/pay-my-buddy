//package com.paymybuddy.demo.controllerTest;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.paymybuddy.demo.controller.rest.LoginController;
//import com.paymybuddy.demo.filter.JWTFilter;
//import com.paymybuddy.demo.model.LoginRequest;
//import com.paymybuddy.demo.model.User;
//import com.paymybuddy.demo.service.JWTService;
//import com.paymybuddy.demo.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//@WebMvcTest(controllers = LoginController.class)
//@Import(LoginControllerTest.TestSecurityConfig.class)
//class LoginControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private JWTService jwtService;
//
//    @MockBean
//    private AuthenticationManager authenticationManager;
//
//    @MockBean
//    private UserService userService;
//
//    @MockBean
//    private JWTFilter jwtFilter;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    @WithMockUser
//    void login_ShouldReturnToken_WhenCredentialsAreValid() throws Exception {
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setEmail("test@example.com");
//        loginRequest.setPassword("password123");
//
//        User user = new User();
//        user.setEmail("test@example.com");
//        user.setUsername("testUser");
//
//        Authentication authentication = mock(Authentication.class);
//
//        when(userService.findUserByEmail("test@example.com")).thenReturn(user);
//        when(authenticationManager.authenticate(any())).thenReturn(authentication);
//        when(jwtService.generateToken(authentication)).thenReturn("mockedToken");
//
//        mockMvc.perform(post("/api/users/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.token").value("mockedToken"));
//    }
//
//    @Test
//    @WithMockUser
//    void login_ShouldReturnUnauthorized_WhenUserNotFound() throws Exception {
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setEmail("unknown@example.com");
//        loginRequest.setPassword("password123");
//
//        when(userService.findUserByEmail("unknown@example.com"))
//                .thenThrow(new IllegalArgumentException("User not found"));
//
//        ResultActions resultActions = mockMvc.perform(post("/api/users/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isUnauthorized())
//                .andExpect(content().string("Identifiants incorrects"));
//    }
//
//    @Test
//    @WithMockUser
//    void login_ShouldReturnUnauthorized_WhenBadCredentials() throws Exception {
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setEmail("test@example.com");
//        loginRequest.setPassword("wrongpassword");
//
//        User user = new User();
//        user.setEmail("test@example.com");
//
//        when(userService.findUserByEmail("test@example.com")).thenReturn(user);
//        when(authenticationManager.authenticate(any()))
//                .thenThrow(new BadCredentialsException("Bad credentials"));
//
//        mockMvc.perform(post("/api/users/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginRequest)))
//                .andExpect(status().isUnauthorized())
//                .andExpect(content().string("Identifiants incorrects"));
//    }
//
//    @TestConfiguration
//    static class TestSecurityConfig {
//        @Bean
//        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//            http.csrf(csrf -> csrf.disable())
//                    .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
//                    .securityMatcher(request -> false); // <<< Ajoute ça pour éviter les filtres
//            return http.build();
//        }
//    }
//
//
//    @Bean
//        public UserDetailsService userDetailsService() {
//            return username -> null; // Dummy UserDetailsService
//        }
//    }
//}
//
//
//
//
