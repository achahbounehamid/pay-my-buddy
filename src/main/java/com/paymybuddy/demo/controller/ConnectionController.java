package com.paymybuddy.demo.controller;

import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/connections")
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

//    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public ResponseEntity<String> addFriend(@RequestParam String friendEmail, Authentication authentication) {
        // Vérification du paramètre friendEmail
        if (friendEmail == null || friendEmail.isEmpty()) {
            return ResponseEntity.badRequest().body("Friend email is required");
        }
        // Appeler le service pour ajouter un ami
        try {
            connectionService.addFriend(authentication.getName(), friendEmail);
            return ResponseEntity.ok("Friend added successfully!");

        } catch (IllegalArgumentException e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping()
    public ResponseEntity<List<User>> getFriends(Authentication authentication) {
        String username = authentication.getName(); // Utilisateur authentifié
        List<User> friends = connectionService.getFriends(username);
        return ResponseEntity.ok(friends); // Retourne la liste des amis
    }
    @DeleteMapping()
    public String removeFriend() {
//        String username = authentication.getName(); // Utilisateur authentifié
//        connectionService.removeFriend(username, friendEmail);
//        return ResponseEntity.ok("Friend removed successfully!");
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
//        System.out.println(userDetails);
        return "User deleted successfully!";

    }
}

