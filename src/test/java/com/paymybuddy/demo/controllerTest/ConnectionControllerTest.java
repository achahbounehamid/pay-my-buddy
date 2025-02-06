package com.paymybuddy.demo.controllerTest;

import com.paymybuddy.demo.controller.ConnectionController;
import com.paymybuddy.demo.service.ConnectionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ConnectionControllerTest {

    @Mock
    private ConnectionService connectionService;

    @InjectMocks
    private ConnectionController connectionController;

    @Mock
    private Authentication authentication;

    @Test
    void addFriend_Success() {
        String friendEmail = "friend@example.com";
        String username = "user1";
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn(username);

        // Simuler la logique de la méthode addFriend
        doNothing().when(connectionService).addFriend(username, friendEmail);

        ResponseEntity<String> response = connectionController.addFriend(friendEmail, authentication);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Friend added successfully!", response.getBody());
    }
    @Test
    void addFriend_FriendAlreadyConnected() {
        String friendEmail = "friend@example.com";
        String username = "user1";
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn(username);

        // Simuler l'exception pour les amis déjà connectés
        doThrow(new IllegalArgumentException("Already connected with this friend."))
                .when(connectionService).addFriend(username, friendEmail);

        ResponseEntity<String> response = connectionController.addFriend(friendEmail, authentication);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Already connected with this friend.", response.getBody());
    }


}
