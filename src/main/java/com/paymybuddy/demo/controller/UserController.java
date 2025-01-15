package com.paymybuddy.demo.controller;

import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

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


    @PostMapping
    public User createUser(@RequestBody  User user) {
        return userService.saveUser(user);
    }
}
