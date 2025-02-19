package com.paymybuddy.demo.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.paymybuddy.demo.DTO.UserDTO;
import com.paymybuddy.demo.controller.rest.UserController;
import com.paymybuddy.demo.filter.JWTFilter;
import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.service.JWTService;
import com.paymybuddy.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(UserControllerTest.SecurityOverrideConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JWTFilter jwtFilter;

    @MockBean
    private JWTService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getUserByUsername_ShouldReturnUser() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("JohnDoe");
        user.setEmail("john@example.com");
        user.setPassword("password123");
        user.setBalance(BigDecimal.ZERO);
        user.setRoles(Set.of("ROLE_USER"));
        user.setConnections(new HashSet<>());
        user.setSentTransactions(new HashSet<>());
        user.setReceivedTransactions(new HashSet<>());

        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail());

        when(userService.findUserByUsername("JohnDoe")).thenReturn(user);

        mockMvc.perform(get("/api/users/username/JohnDoe"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDTO)));
    }

    @Test
    @WithMockUser
    void registerUser_ShouldReturnCreated() throws Exception {
        User user = new User();
        user.setUsername("NewUser");
        user.setEmail("newuser@example.com");
        user.setPassword("password123");
        user.setBalance(BigDecimal.ZERO);
        user.setRoles(Set.of("ROLE_USER"));
        user.setConnections(new HashSet<>());
        user.setSentTransactions(new HashSet<>());
        user.setReceivedTransactions(new HashSet<>());

        doNothing().when(userService).registerUser(any(User.class));

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully!"));

        verify(userService, times(1)).registerUser(any(User.class));
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
