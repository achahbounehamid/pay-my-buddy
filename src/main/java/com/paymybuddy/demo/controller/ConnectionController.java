package com.paymybuddy.demo.controller;
import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/connections")
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/add")
    public ResponseEntity<String> addFriend(@RequestParam String friendEmail, Authentication authentication) {
        connectionService.addFriend(authentication.getName(), friendEmail);
        return ResponseEntity.ok("Friend added successfully!");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<User>> getFriends(Authentication authentication) {
        List<User> friends = connectionService.getFriends(authentication.getName());
        return ResponseEntity.ok().body(friends);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFriend(@RequestParam String friendEmail, Authentication authentication) {
        connectionService.removeFriend(authentication.getName(), friendEmail);
        return ResponseEntity.ok("Friend removed successfully");
    }
}

