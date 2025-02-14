package com.paymybuddy.demo.controller;

import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/connections")
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    @PostMapping("/add")
    public ResponseEntity<String> addFriend(@RequestParam String friendEmail, Authentication authentication) {
        // Vérification du paramètre friendEmail
//        if (friendEmail == null || friendEmail.isEmpty())
        if (!StringUtils.hasText(friendEmail)){
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
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getFriends(Authentication authentication) {
        String email = authentication.getName();
        List<User> friends = connectionService.getFriends(email);

        List<Map<String, Object>> result = friends.stream()
                .map(friend -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", friend.getId());
                    map.put("email", friend.getEmail());
                    map.put("username", friend.getUsername());
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
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

