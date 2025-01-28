package com.paymybuddy.demo.service;

import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.repository.UserRepository;
import com.paymybuddy.demo.repository.ConnectionsRepository; // Ajoutez l'import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;

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
    public  List<User> findAllUsers(){
        return  userRepository.findAll();
    }
    public void registerUser(User user) {
        // confirmer l'appel à la méthode
        System.out.println("registerUser method called for: " + user.getEmail());
        // Vérification si l'utilisateur existe déjà
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use!");
        }

        // Hash du mot de passe
        // Log avant l'encodage du mot de passe
        System.out.println("Encoding password for user: " + user.getEmail());
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
       // Rôle par défaut
        System.out.println("Assigning default role (ROLE_USER) to user: " + user.getEmail());
        user.setRoles(Collections.singleton("ROLE_USER"));

        // Enregistrement
        System.out.println("Saving user to database: " + user.getEmail());
        userRepository.save(user);

        // Log pour confirmer l'enregistrement
        System.out.println("User registered: " + user.getEmail());
    }

    public  User findUserById(int id){
        return  userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
    }

    public void updateUser(int id, User updatedUser){
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));

        //Miss à jour des champs (pas de mot de passe à jour ici)
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());

        //Sauvegarde des modification
        userRepository.save(existingUser);
    }
    public void deleteUser(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID:" + id ));
        userRepository.delete(user);
    }

}
