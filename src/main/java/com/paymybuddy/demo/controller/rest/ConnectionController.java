package com.paymybuddy.demo.controller.rest;

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

    /**
     * Constructor to initialize the ConnectionService.
     *
     * @param connectionService Service for managing user connections.
     */
    @Autowired
    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }
    /**
     * Endpoint to add a friend by email.
     *
     * @param friendEmail      The email of the friend to add.
     * @param authentication   The authentication object containing the current user's details.
     * @return ResponseEntity with success or error message.
     */
    @PostMapping("/add")
    public ResponseEntity<String> addFriend(@RequestParam String friendEmail, Authentication authentication) {
        // Get the current user's email from authentication
        String currentUserEmail = authentication.getName();

        // Validate the friendEmail parameter
        if (!StringUtils.hasText(friendEmail)){
            return ResponseEntity.badRequest().body("Friend email is required");
        }
        // Call the service to add a friend
        try {


            connectionService.addFriend(currentUserEmail, friendEmail);
            return ResponseEntity.ok("Friend added successfully!");

        } catch (IllegalArgumentException e) {
            // Log the exception and return a bad request response
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Endpoint to retrieve a list of friends.
     *
     * @param authentication The authentication object containing the current user's details.
     * @return ResponseEntity with a list of friends.
     */
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getFriends(Authentication authentication) {

        String email = authentication.getName();
        List<User> friends = connectionService.getFriends(email);
        // Transform the list of User objects into a list of maps
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
    /**
     * Endpoint to remove a friend.
     *
     * @return Success message.
     */
    @DeleteMapping()
    public String removeFriend() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        // Implement the logic to remove a friend
        // For now, return a success message
        return "User deleted successfully!";
    }
}

