package com.paymybuddy.demo.serviceTest;

import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.repository.UserRepository;
import com.paymybuddy.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findUserByUsername_ShouldReturnUser() {
        User user = new User();
        user.setUsername("alex");

        when(userRepository.findByUsername("alex")).thenReturn(Optional.of(user));

        User result = userService.findUserByUsername("alex");

        assertNotNull(result);
        assertEquals("alex", result.getUsername());
    }

    @Test
    void findUserByUsername_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByUsername("UnknownUser")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.findUserByUsername("UnknownUser")
        );

        assertEquals("User with username UnknownUser not found", exception.getMessage());
    }

    @Test
    void registerUser_ShouldHashPasswordAndSave() {
        User user = new User();
        user.setUsername("NewUser");
        user.setPassword("plainPassword");

        when(userRepository.findByUsername("NewUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainPassword")).thenReturn("hashedPassword");

        userService.registerUser(user);

        assertEquals("hashedPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void registerUser_ShouldThrowException_WhenUsernameExists() {
        User existingUser = new User();
        existingUser.setUsername("ExistingUser");

        when(userRepository.findByUsername("ExistingUser")).thenReturn(Optional.of(existingUser));

        User newUser = new User();
        newUser.setUsername("ExistingUser");

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.registerUser(newUser)
        );

        assertEquals("Username already in use!", exception.getMessage());
    }

    @Test
    void findUserById_ShouldReturnUser() {
        User user = new User();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.findUserById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void findUserById_ShouldThrowException_WhenNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.findUserById(1)
        );

        assertEquals("User not found with ID: 1", exception.getMessage());
    }

    @Test
    void deleteUser_ShouldDeleteUser() {
        User user = new User();
        user.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.deleteUser(1);

        verify(userRepository, times(1)).delete(user);
    }

    //rajoute updateUser et deleteUserByUsername

}
