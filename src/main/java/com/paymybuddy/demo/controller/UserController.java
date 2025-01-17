package com.paymybuddy.demo.controller;

import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.service.JwtService;
import com.paymybuddy.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/{email}")
    public User getUserByEmail(@PathVariable String email) {
        System.out.println("Requête reçue pour l'utilisateur avec l'email: " + email);
        try {
            User user = userService.findUserByEmail(email);
            System.out.println("Utilisateur trouvé: " + user);
            return user;
        } catch (RuntimeException e) {
            System.out.println("Erreur lors de la récupération de l'utilisateur: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        // Authentifier l'utilisateur
        // Si l'authentification réussit, renvoyer un JWT
        String token = jwtService.generateToken(email); // Supposons que vous générez le token en utilisant l'email de l'utilisateur
        return ResponseEntity.ok().body("Bearer " + token);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }


    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
