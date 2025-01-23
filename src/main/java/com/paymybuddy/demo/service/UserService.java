package com.paymybuddy.demo.service;

import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.repository.UserRepository;
import com.paymybuddy.demo.repository.ConnectionsRepository; // Ajoutez l'import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConnectionsRepository connectionRepository; // Ajoutez cette ligne

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User with email " + email + " not found"));
    }

    public List<User> getConnectionsByEmail(String email) {
        // Trouver l'utilisateur par son email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Trouver les connexions de cet utilisateur
        List<User> connections = connectionRepository.findConnectionsByUserId(user.getId());

        return connections;
    }
}
