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
    /**
     * Set up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test that finding a user by username returns the correct user.
     */
    @Test
    void findUserByUsername_ShouldReturnUser() {
        // Arrange: Create a mock user
        User user = new User();
        user.setUsername("alex");
        // Mock repository behavior
        when(userRepository.findByUsername("alex")).thenReturn(Optional.of(user));
        // Act: Retrieve the user by username
        User result = userService.findUserByUsername("alex");
        // Assert: Verify the user is found and has the correct username
        assertNotNull(result);
        assertEquals("alex", result.getUsername());
    }
    /**
     * Test that finding a user by username throws an exception when the user is not found.
     */

    @Test
    void findUserByUsername_ShouldThrowException_WhenUserNotFound() {
        // Arrange: Mock repository behavior to return an empty optional
        when(userRepository.findByUsername("UnknownUser")).thenReturn(Optional.empty());
        // Act & Assert: Verify exception is thrown with the correct message
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.findUserByUsername("UnknownUser")
        );

        assertEquals("User with username UnknownUser not found", exception.getMessage());
    }
    /**
     * Test that registering a user hashes the password and saves the user.
     */
    @Test
    void registerUser_ShouldHashPasswordAndSave() {
        // Arrange: Create a mock user
        User user = new User();
        user.setUsername("NewUser");
        user.setPassword("plainPassword");
        // Mock repository and encoder behavior
        when(userRepository.findByUsername("NewUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainPassword")).thenReturn("hashedPassword");
         // Act: Register the user
        userService.registerUser(user);
         // Assert: Verify the password is hashed and the user is saved
        assertEquals("hashedPassword", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }
    /**
     * Test that registering a user throws an exception when the username already exists.
     */
    @Test
    void registerUser_ShouldThrowException_WhenUsernameExists() {
        // Arrange: Create an existing user
        User existingUser = new User();
        existingUser.setUsername("ExistingUser");
        // Mock repository behavior
        when(userRepository.findByUsername("ExistingUser")).thenReturn(Optional.of(existingUser));
        // Act & Assert: Verify exception is thrown with the correct message
        User newUser = new User();
        newUser.setUsername("ExistingUser");

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.registerUser(newUser)
        );

        assertEquals("Username already in use!", exception.getMessage());
    }
    /**
     * Test that finding a user by ID returns the correct user.
     */
    @Test
    void findUserById_ShouldReturnUser() {
        // Arrange: Create a mock user
        User user = new User();
        user.setId(1);
        // Mock repository behavior
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        // Act: Retrieve the user by ID
        User result = userService.findUserById(1);
        // Assert: Verify the user is found and has the correct ID
        assertNotNull(result);
        assertEquals(1, result.getId());
    }
    /**
     * Test that finding a user by ID throws an exception when the user is not found.
     */
    @Test
    void findUserById_ShouldThrowException_WhenNotFound() {
        // Arrange: Mock repository behavior to return an empty optional
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        // Act & Assert: Verify exception is thrown with the correct message
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.findUserById(1)
        );

        assertEquals("User not found with ID: 1", exception.getMessage());
    }
    /**
     * Test that deleting a user deletes the user correctly.
     */
    @Test
    void deleteUser_ShouldDeleteUser() {
        // Arrange: Create a mock user
        User user = new User();
        user.setId(1);
        // Mock repository behavior
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        // Act: Delete the user
        userService.deleteUser(1);
       // Assert: Verify the user is deleted
        verify(userRepository, times(1)).delete(user);
    }

}
