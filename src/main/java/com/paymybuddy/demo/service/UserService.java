package com.paymybuddy.demo.service;

import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Service class for managing user-related operations.
 * This class implements UserDetailsService to integrate with Spring Security.
 */
@Service
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user to retrieve.
     * @return The User entity if found.
     * @throws IllegalArgumentException if the user is not found.
     */
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User with username " + username + " not found"));
    }
    /**
     * Retrieves a user by their email address.
     *
     * @param email The email address of the user to retrieve.
     * @return The User entity if found.
     * @throws IllegalArgumentException if the user is not found.
     */
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User with email " + email + " not found"));
    }
    /**
     * Retrieves all users (for admin use).
     *
     * @return A list of all User entities.
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    /**
     * Registers a new user.
     *
     * @param user The user object containing registration details.
     * @throws IllegalArgumentException if the username is already in use.
     */
    @Transactional
    public void registerUser(User user) {
        logger.error("Username already in use: " + user.getUsername());
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {

            throw new IllegalArgumentException("Username already in use!");
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userRepository.save(user);

        logger.info("User saved in database.");
    }
    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The User entity if found.
     * @throws IllegalArgumentException if the user is not found.
     */
    public User findUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
    }
    /**
     * Updates a user by their ID.
     *
     * @param id         The ID of the user to update.
     * @param updatedUser The updated user details.
     * @throws IllegalArgumentException if the user is not found.
     */
    public void updateUser(int id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());

        userRepository.save(existingUser);
    }
    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     * @throws IllegalArgumentException if the user is not found.
     */
    public void deleteUser(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        userRepository.delete(user);
    }
    /**
     * Updates the currently logged-in user by their email.
     *
     * @param email      The email of the user to update.
     * @param updatedUser The updated user details.
     * @throws IllegalArgumentException if the user is not found.
     */
    @Transactional
    public void updateUserByUserEmail(String email, User updatedUser) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));


        boolean updated = false;

        if (updatedUser.getUsername() != null && !updatedUser.getUsername().isBlank()) {
            logger.info("Updating username to: {}", updatedUser.getUsername());
            existingUser.setUsername(updatedUser.getUsername());
            updated = true;
        }

        if (updatedUser.getEmail() != null && !updatedUser.getEmail().isBlank()) {
            logger.info("Updating email to: {}", updatedUser.getEmail());
            existingUser.setEmail(updatedUser.getEmail());
            updated = true;
        }

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            logger.info(" Updating password.");
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword().trim()));
            updated = true;
        }

        if (updatedUser.getBalance() != null) {
            if (updatedUser.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Balance cannot be negative.");
            }
            logger.info("Updating balance to: {}", updatedUser.getBalance());
            existingUser.setBalance(updatedUser.getBalance());
            updated = true;
        }

        if (updatedUser.getRoles() != null && !updatedUser.getRoles().isEmpty()) {
            logger.info(" Updating roles.");
            existingUser.setRoles(updatedUser.getRoles());
            updated = true;
        }

        if (updated) {
            logger.info(" Saving user modifications: {}", existingUser);
            userRepository.save(existingUser);
            logger.info("User updated successfully!");
        } else {
            logger.warn("⚠No changes detected, update ignored.");
        }
    }


    /**
     * Deletes the currently logged-in user by their username.
     *
     * @param username The username of the user to delete.
     * @throws IllegalArgumentException if the user is not found.
     */
    public void deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        userRepository.delete(user);
    }
    /**
     * Loads user details for Spring Security authentication.
     *
     * @param username The username of the user to load.
     * @return UserDetails object for Spring Security.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info(" Chargement des détails de l'utilisateur pour l'email : " + username);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable : " + username));
        logger.info(" Utilisateur trouvé : " + user.getEmail());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

}

