
package com.paymybuddy.demo.serviceTest;

import com.paymybuddy.demo.model.Connections;
import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.repository.ConnectionsRepository;
import com.paymybuddy.demo.repository.UserRepository;
import com.paymybuddy.demo.service.ConnectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConnectionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConnectionsRepository connectionRepository;

    @InjectMocks
    private ConnectionService connectionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addFriend_ShouldAddFriendSuccessfully() {
        User user = new User();
        user.setId(1);
        user.setEmail("user@example.com");

        User friend = new User();
        friend.setId(2);
        friend.setEmail("friend@example.com");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("friend@example.com")).thenReturn(Optional.of(friend));
        when(connectionRepository.findByUserAndFriend(user, friend)).thenReturn(Optional.empty());

        connectionService.addFriend("user@example.com", "friend@example.com");

        verify(connectionRepository, times(2)).save(any(Connections.class));
    }

    @Test
    void getFriends_ShouldReturnFriendList() {
        User user = new User();
        user.setEmail("user@example.com");

        User friend1 = new User();
        friend1.setEmail("friend1@example.com");

        User friend2 = new User();
        friend2.setEmail("friend2@example.com");

        Set<User> friends = new HashSet<>();
        friends.add(friend1);
        friends.add(friend2);

        user.setConnections(friends);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));

        List<User> result = connectionService.getFriends("user@example.com");

        assertEquals(2, result.size());
        assertTrue(result.contains(friend1));
        assertTrue(result.contains(friend2));
    }

    @Test
    void removeFriend_ShouldRemoveFriendSuccessfully() {
        User user = new User();
        user.setId(1);
        user.setEmail("user@example.com");

        User friend = new User();
        friend.setId(2);
        friend.setEmail("friend@example.com");

        Connections connection1 = new Connections();
        connection1.setUser(user);
        connection1.setFriend(friend);

        Connections connection2 = new Connections();
        connection2.setUser(friend);
        connection2.setFriend(user);

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("friend@example.com")).thenReturn(Optional.of(friend));
        when(connectionRepository.findByUserAndFriend(user, friend)).thenReturn(Optional.of(connection1));
        when(connectionRepository.findByUserAndFriend(friend, user)).thenReturn(Optional.of(connection2));

        connectionService.removeFriend("user@example.com", "friend@example.com");

        verify(connectionRepository, times(1)).delete(connection1);
        verify(connectionRepository, times(1)).delete(connection2);
    }

    @Test
    void addFriend_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                connectionService.addFriend("user@example.com", "friend@example.com")
        );

        assertEquals("User not found: user@example.com", exception.getMessage());
    }

    @Test
    void addFriend_ShouldThrowException_WhenFriendNotFound() {
        User user = new User();
        user.setEmail("user@example.com");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("friend@example.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                connectionService.addFriend("user@example.com", "friend@example.com")
        );

        assertEquals("Friend not found: friend@example.com", exception.getMessage());
    }
}
