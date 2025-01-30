package com.paymybuddy.demo.service;

import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.repository.UserRepository;
import com.paymybuddy.demo.repository.ConnectionsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ConnectionsRepository connectionRepository;

    //  Recherche par username
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User with username " + username + " not found"));
    }

    public List<User> getConnectionsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        return connectionRepository.findConnectionsByUserId(user.getId());
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void registerUser(User user) {
        logger.info("registerUser method called for: " + user.getUsername());

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already in use!");
        }

        logger.info("Encoding password for user: " + user.getUsername());
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        logger.info("Assigning default role (ROLE_USER) to user: " + user.getUsername());
        user.setRoles(Collections.singleton("ROLE_USER"));

        logger.info("Saving user to database: " + user.getUsername());
        userRepository.save(user);

        logger.info("User registered: " + user.getUsername());
    }

    public User findUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
    }

    public void updateUser(int id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());

        userRepository.save(existingUser);
    }

    public void deleteUser(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        userRepository.delete(user);
    }

    //  Utilisation de username pour l'authentification
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Recherche de l'utilisateur avec le username : " + username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable : " + username));


        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
