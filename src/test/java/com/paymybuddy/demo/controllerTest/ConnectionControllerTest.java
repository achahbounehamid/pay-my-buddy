package com.paymybuddy.demo.controllerTest;

import com.paymybuddy.demo.controller.rest.ConnectionController;
import com.paymybuddy.demo.service.ConnectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.springframework.test.web.servlet.MockMvc;

class ConnectionControllerTest {
    private ConnectionController connectionController;
    private ConnectionService connectionService;
    private Authentication authentication;
    @Autowired
    private MockMvc mockMvc;
    @BeforeEach
    void setup() {
        // Simulating an authenticated user
        connectionService = Mockito.mock(ConnectionService.class);
        authentication = Mockito.mock(Authentication.class);

        // Creating the controller instance using its constructor
        connectionController = new ConnectionController(connectionService);

        // Simulating an authenticated user
        when(authentication.getName()).thenReturn("user@example.com");
    }
    @Test
    void testAddFriend_ShouldReturnOk() {
        // Simulating adding a friend
        doNothing().when(connectionService).addFriend("user@example.com", "friend@example.com");

        // Directly calling the controller method
        ResponseEntity<String> response = connectionController.addFriend("friend@example.com", authentication);

        // Assertions
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Friend added successfully!", response.getBody());

        verify(connectionService, times(1)).addFriend("user@example.com", "friend@example.com");
    }
}