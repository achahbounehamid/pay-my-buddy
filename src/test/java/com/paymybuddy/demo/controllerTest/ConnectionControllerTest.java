//package com.paymybuddy.demo.controllerTest;
//
//import com.paymybuddy.demo.controller.rest.ConnectionController;
//import com.paymybuddy.demo.filter.JWTFilter;
//import com.paymybuddy.demo.model.User;
//import com.paymybuddy.demo.service.ConnectionService;
//import com.paymybuddy.demo.service.JWTService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.MediaType;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.List;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ConnectionController.class)
//@Import(ConnectionControllerTest.SecurityOverrideConfig.class)
//class ConnectionControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ConnectionService connectionService;
//
//    @MockBean
//    private JWTFilter jwtFilter;
//
//    @MockBean
//    private JWTService jwtService;
//
//    @Test
//    @WithMockUser(username = "user@example.com", roles = {"USER"})
//    void testAddFriend_ShouldReturnOk() throws Exception {
//        mockMvc.perform(post("/api/connections/add")
//                        .param("friendEmail", "friend@example.com")
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Friend added successfully!"));
//
//        verify(connectionService, times(1)).addFriend("user@example.com", "friend@example.com");
//    }
//
//
//    @Test
//    @WithMockUser(username = "user@example.com")
//    void testAddFriend_ShouldReturnBadRequest_WhenFriendEmailIsEmpty() throws Exception {
//        mockMvc.perform(post("/api/connections/add")
//                        .param("friendEmail", "")
//                        .contentType("application/x-www-form-urlencoded"))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Friend email is required"));
//
//        verifyNoInteractions(connectionService);
//    }
//
//    @Test
//    @WithMockUser(username = "user@example.com")
//    void testAddFriend_ShouldReturnBadRequest_WhenServiceThrowsException() throws Exception {
//        doThrow(new IllegalArgumentException("Friend not found"))
//                .when(connectionService).addFriend("user@example.com", "friend@example.com");
//
//        mockMvc.perform(post("/api/connections/add")
//                        .param("friendEmail", "friend@example.com")
//                        .contentType("application/x-www-form-urlencoded"))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Friend not found"));
//
//        verify(connectionService, times(1)).addFriend("user@example.com", "friend@example.com");
//    }
//
//    @Test
//    @WithMockUser(username = "user@example.com")
//    void testGetFriends_ShouldReturnEmptyList() throws Exception {
//        when(connectionService.getFriends("user@example.com")).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get("/api/connections"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json"))
//                .andExpect(content().json("[]"));
//
//        verify(connectionService, times(1)).getFriends("user@example.com");
//    }
//
//    @Test
//    @WithMockUser(username = "user@example.com")
//    void testGetFriends_WithData() throws Exception {
//        User friend = new User();
//        friend.setId(1);
//        friend.setEmail("friend@example.com");
//        friend.setUsername("Friend");
//        // Pour éviter les erreurs lors de la sérialisation
//        friend.setConnections(new HashSet<>());
//        friend.setSentTransactions(new HashSet<>());
//        friend.setReceivedTransactions(new HashSet<>());
//
//        when(connectionService.getFriends("user@example.com")).thenReturn(List.of(friend));
//
//        mockMvc.perform(get("/api/connections"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json"))
//                .andExpect(content().json("""
//                    [
//                        {
//                            "id": 1,
//                            "email": "friend@example.com",
//                            "username": "Friend"
//                        }
//                    ]
//                    """));
//
//        verify(connectionService, times(1)).getFriends("user@example.com");
//    }
//
//    @TestConfiguration
//    static class SecurityOverrideConfig {
//        @Bean
//        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//            http.csrf().disable()
//                    .authorizeHttpRequests().anyRequest().permitAll();
//            return http.build();
//        }
//    }
//}
