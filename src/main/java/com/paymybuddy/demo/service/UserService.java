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

import java.util.Collections;
import java.util.List;


@Service
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User with username " + username + " not found"));
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void registerUser(User user) {
        logger.info("registerUser method called for: " + user.getUsername());

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already in use!");
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable : " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}

