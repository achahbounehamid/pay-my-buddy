package com.paymybuddy.demo.serviceTest;

import com.paymybuddy.demo.model.Connections;
import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.repository.ConnectionsRepository;
import com.paymybuddy.demo.repository.UserRepository;
import com.paymybuddy.demo.service.ConnectionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConnectionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConnectionsRepository connectionRepository;

    @InjectMocks
    private ConnectionService connectionService;
    /**
     * Test that adding a friend is successful when both users exist and are not already connected.
     */
    @Test
    void addFriend_ShouldAddFriendSuccessfully() {
        // Arrange: Create mock users
        User user = new User();
        user.setId(1);
        user.setEmail("user@example.com");

        User friend = new User();
        friend.setId(2);
        friend.setEmail("friend@example.com");
// Mock repository behavior
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("friend@example.com")).thenReturn(Optional.of(friend));
        when(connectionRepository.findByUserAndFriend(user, friend)).thenReturn(Optional.empty());
        // Act: Add friend
        connectionService.addFriend("user@example.com", "friend@example.com");

        verify(connectionRepository, times(2)).save(any(Connections.class));
    }
    /**
     * Test that retrieving a list of friends returns the correct list.
     */
    @Test
    void getFriends_ShouldReturnFriendList() {
        // Arrange: Create mock users and their connections
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
        // Mock repository behavior
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        // Act: Retrieve friends
        List<User> result = connectionService.getFriends("user@example.com");
       // Assert: Verify the correct friends are returned
        assertEquals(2, result.size());
        assertTrue(result.contains(friend1));
        assertTrue(result.contains(friend2));
    }
    /**
     * Test that removing a friend is successful when both users and connections exist.
     */
    @Test
    void removeFriend_ShouldRemoveFriendSuccessfully() {
        // Arrange: Create mock users and their connections
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
        // Mock repository behavior
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("friend@example.com")).thenReturn(Optional.of(friend));
        when(connectionRepository.findByUserAndFriend(user, friend)).thenReturn(Optional.of(connection1));
        when(connectionRepository.findByUserAndFriend(friend, user)).thenReturn(Optional.of(connection2));
        // Act: Remove friend
        connectionService.removeFriend("user@example.com", "friend@example.com");
        // Assert: Verify that connections are deleted
        verify(connectionRepository, times(1)).delete(connection1);
        verify(connectionRepository, times(1)).delete(connection2);
    }
    /**
     * Test that adding a friend throws an exception when the user is not found.
     */
    @Test
    void addFriend_ShouldThrowException_WhenUserNotFound() {
        // Arrange: Mock repository behavior to return an empty optional
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());
        // Act & Assert: Verify exception is thrown with the correct message
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                connectionService.addFriend("user@example.com", "friend@example.com")
        );

        assertEquals("User not found: user@example.com", exception.getMessage());
    }
    /**
     * Test that adding a friend throws an exception when the friend is not found.
     */
    @Test
    void addFriend_ShouldThrowException_WhenFriendNotFound() {
        // Arrange: Create mock user and mock repository behavior
        User user = new User();
        user.setEmail("user@example.com");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("friend@example.com")).thenReturn(Optional.empty());
        // Act & Assert: Verify exception is thrown with the correct message
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                connectionService.addFriend("user@example.com", "friend@example.com")
        );

        assertEquals("Friend not found: friend@example.com", exception.getMessage());
    }
    /**
     * Test that adding a friend throws an exception when the friend is already added.
     */
    @Test
    void addFriend_ShouldThrowException_WhenFriendAlreadyAdded() {
        // Arrange: Create mock users and existing connection
        User user = new User();
        user.setId(1);
        user.setEmail("user@example.com");

        User friend = new User();
        friend.setId(2);
        friend.setEmail("friend@example.com");

        Connections existingConnection = new Connections();
        existingConnection.setUser(user);
        existingConnection.setFriend(friend);

        // Mock repository behavior
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("friend@example.com")).thenReturn(Optional.of(friend));
        when(connectionRepository.findByUserAndFriend(user, friend)).thenReturn(Optional.of(existingConnection));
        // Act & Assert: Verify exception is thrown with the correct message
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                connectionService.addFriend("user@example.com", "friend@example.com")
        );

        assertEquals("Already connected with this friend.", exception.getMessage()); // Mettre à jour le message attendu
    }
    /**
     * Test that adding a friend saves the connections correctly.
     */
    @Test
    void addFriend_ShouldSaveConnectionsCorrectly() {
        // Arrange: Create mock users
        User user = new User();
        user.setId(1);
        user.setEmail("user@example.com");

        User friend = new User();
        friend.setId(2);
        friend.setEmail("friend@example.com");
        // Mock repository behavior
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("friend@example.com")).thenReturn(Optional.of(friend));
        when(connectionRepository.findByUserAndFriend(user, friend)).thenReturn(Optional.empty());
        // Act: Add friend
        connectionService.addFriend("user@example.com", "friend@example.com");
        // Assert: Verify that connections are saved correctly
        ArgumentCaptor<Connections> connectionsCaptor = ArgumentCaptor.forClass(Connections.class);
        verify(connectionRepository, times(2)).save(connectionsCaptor.capture());

        List<Connections> savedConnections = connectionsCaptor.getAllValues();
        assertEquals(user, savedConnections.get(0).getUser());
        assertEquals(friend, savedConnections.get(0).getFriend());
        assertEquals(friend, savedConnections.get(1).getUser());
        assertEquals(user, savedConnections.get(1).getFriend());
    }
}