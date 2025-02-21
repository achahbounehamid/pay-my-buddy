//package com.paymybuddy.demo.controllerTest;
//
//import com.paymybuddy.demo.controller.rest.ConnectionController;
//import com.paymybuddy.demo.filter.JWTFilter;
//import com.paymybuddy.demo.service.ConnectionService;
//import com.paymybuddy.demo.service.JWTService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Collections;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(ConnectionController.class)
//@AutoConfigureMockMvc(addFilters = false)
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
//    void testAddFriend_ShouldReturnOk() throws Exception {
//        mockMvc.perform(post("/api/connections/add")
//                        .param("friendEmail", "friend@example.com")
//                        .with(SecurityMockMvcRequestPostProcessors.user("user@example.com")))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Friend added successfully!"));
//
//        verify(connectionService).addFriend("user@example.com", "friend@example.com");
//    }
//
//    @Test
//    void testGetFriends_ShouldReturnEmptyList() throws Exception {
//        when(connectionService.getFriends("user@example.com")).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(get("/api/connections")
//                        .with(SecurityMockMvcRequestPostProcessors.user("user@example.com")))
//                .andExpect(status().isOk())
//                .andExpect(content().json("[]"));
//
//        verify(connectionService).getFriends("user@example.com");
//    }
//}
