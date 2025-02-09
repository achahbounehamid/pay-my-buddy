//package com.paymybuddy.demo.serviceTest;
//
//import com.paymybuddy.demo.model.User;
//import com.paymybuddy.demo.model.Connections;
//import com.paymybuddy.demo.repository.UserRepository;
//import com.paymybuddy.demo.repository.ConnectionsRepository;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//
//import static org.mockito.Mockito.*;
//
//import java.util.Arrays;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class ConnectionServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private ConnectionsRepository connectionsRepository;
//
//    @InjectMocks
//    private ConnectionService connectionService;
//
//    @Test
//    void addFriend_Success() {
//        String username = "user1";
//        String friendEmail = "friend@example.com";
//        User user = new User();
//        user.setUsername(username);
//
//        User friend = new User();
//        friend.setEmail(friendEmail);
//
//        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
//        when(userRepository.findByEmail(friendEmail)).thenReturn(Optional.of(friend));
//        when(connectionsRepository.findByUserAndFriend(user, friend)).thenReturn(Optional.empty());
//
//        connectionService.addFriend(username, friendEmail);
//
//        verify(connectionsRepository, times(2)).save(any(Connections.class)); // Vérifie que la méthode save a été appelée deux fois
//    }
//
//    @Test
//    void addFriend_AlreadyConnected() {
//        String username = "user1";
//        String friendEmail = "friend@example.com";
//        User user = new User();
//        user.setUsername(username);
//
//        User friend = new User();
//        friend.setEmail(friendEmail);
//
//        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
//        when(userRepository.findByEmail(friendEmail)).thenReturn(Optional.of(friend));
//        when(connectionsRepository.findByUserAndFriend(user, friend)).thenReturn(Optional.of(new Connections()));
//
//        assertThrows(IllegalArgumentException.class, () -> connectionService.addFriend(username, friendEmail));
//    }
//
//    @Test
//    void getFriends_Success() {
//        String username = "user1";
//        User user = new User();
//        user.setUsername(username);
//        Connections connection = new Connections();
//        connection.setUser(user);
//        user.setConnections(Arrays.asList(connection));
//
//        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
//
//        var result = connectionService.getFriends(username);
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void removeFriend_Success() {
//        String username = "user1";
//        String friendEmail = "friend@example.com";
//        User user = new User();
//        user.setUsername(username);
//
//        User friend = new User();
//        friend.setEmail(friendEmail);
//
//        Connections connection1 = new Connections();
//        connection1.setUser(user);
//        connection1.setFriend(friend);
//
//        Connections connection2 = new Connections();
//        connection2.setUser(friend);
//        connection2.setFriend(user);
//
//        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
//        when(userRepository.findByEmail(friendEmail)).thenReturn(Optional.of(friend));
//        when(connectionsRepository.findByUserAndFriend(user, friend)).thenReturn(Optional.of(connection1));
//        when(connectionsRepository.findByUserAndFriend(friend, user)).thenReturn(Optional.of(connection2));
//
//        connectionService.removeFriend(username, friendEmail);
//
//        verify(connectionsRepository, times(2)).delete(any(Connections.class)); // Vérifie que delete a été appelé deux fois
//    }
//}
