package com.paymybuddy.demo.controller;

import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    //  Récupérer un utilisateur par son username
    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.findUserByUsername(username);
    }
    // Inscription d'un nouvel utilisateur
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user) {
        logger.info("Register endpoint called with user: " + user.getEmail());
        try {
            userService.registerUser(user);
            logger.info("User registered successfully: " + user.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
        } catch (Exception e) {
            e.printStackTrace(); //  l'erreur complète
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }


    }
    // Récupérer tous les utilisateurs (réservé aux ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){

        return  ResponseEntity.ok(userService.findAllUsers());
    }
    // Récupérer un utilisateur par ID (réservé aux ADMIN)
    @PreAuthorize("hasRole('ADMIN)")
    @GetMapping("/id/{id}")
    public  ResponseEntity<User> getUserById(@PathVariable int id){
        User user =userService.findUserById(id);
        return  ResponseEntity.ok(user);
    }
    // Modifier un utilisateur par ID (réservé aux ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/id/{id}")
    public  ResponseEntity<String> updateUser(@PathVariable int id, @Valid @RequestBody User updateUser){
        try {
            userService.updateUser(id, updateUser);
            return ResponseEntity.ok("User updated successfully");
        }catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    // Supprimer un utilisateur par ID (réservé aux ADMIN)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/id/{id}")
    public  ResponseEntity<String> deleteUser(@PathVariable int id){
        try{
            userService.deleteUser(id);
            return  ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return  ResponseEntity.status((HttpStatus.BAD_REQUEST)).body("Error:" + e.getMessage());
        }
    }
    // Récupérer les infos de l'utilisateur connecté
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(Authentication authentication){
        String username = authentication.getName();
        return  ResponseEntity.ok(userService.findUserByUsername(username));

    }
    //  Modifier son propre compte
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/me")
    public ResponseEntity<String> updateCurrentUser(Authentication authentication, @Valid @RequestBody User updateUser){
        String username = authentication.getName();
        logger.info("Mise à jour des infos de l'utilisateur: {}", username);
        try {
            userService.updateUserByUsername(username, updateUser);
            return ResponseEntity.ok("User updated successfully!");
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    // Supprimer son propre compte
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        try {
            userService.deleteUserByUsername(username);
            return ResponseEntity.ok("User deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

}
