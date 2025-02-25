package com.paymybuddy.demo.controller.rest;

import com.paymybuddy.demo.DTO.UserDTO;
import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.service.UserService;
import com.paymybuddy.demo.validation.OnCreate;
import com.paymybuddy.demo.validation.OnUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    /**
     * Endpoint to get a user by username.
     *
     * @param username The username of the user.
     * @return UserDTO containing user details.
     */
    @GetMapping("/username/{username}")
    public UserDTO getUserByUsername(@PathVariable String username) {
        User user = userService.findUserByUsername(username);
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
    }

    /**
     * Endpoint to register a new user.
     *
     * @param user The user object containing registration details.
     * @return ResponseEntity with success or error message.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Validated(OnCreate.class) @RequestBody User user) {
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
    /**
     * Endpoint to retrieve all users (admin only).
     *
     * @return ResponseEntity with a list of all users.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){

        return  ResponseEntity.ok(userService.findAllUsers());
    }
    /**
     * Endpoint to retrieve a user by ID (admin only).
     *
     * @param id The ID of the user.
     * @return ResponseEntity with user details.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/id/{id}")
    public  ResponseEntity<User> getUserById(@PathVariable int id){
        User user =userService.findUserById(id);
        return  ResponseEntity.ok(user);
    }
    /**
     * Endpoint to update a user by ID (admin only).
     *
     * @param id         The ID of the user to update.
     * @param updateUser The updated user details.
     * @return ResponseEntity with success or error message.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/id/{id}")
    public  ResponseEntity<String> updateUser(@PathVariable int id, @Validated(OnUpdate.class) @RequestBody User updateUser){
        try {
            userService.updateUser(id, updateUser);
            return ResponseEntity.ok("User updated successfully");
        }catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    /**
     * Endpoint to delete a user by ID (admin only).
     *
     * @param id The ID of the user to delete.
     * @return ResponseEntity with success or error message.
     */
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
    /**
     * Endpoint to retrieve the current user's information.
     *
     * @param authentication The authentication object.
     * @return ResponseEntity with user details or error message.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "Utilisateur non trouvé"));
        }
        return ResponseEntity.ok(user);
    }
    /**
     * Endpoint to update the current user's information.
     *
     * @param authentication The authentication object.
     * @param updateUser     The updated user details.
     * @return ResponseEntity with success or error message.
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/me")
    public ResponseEntity<String> updateCurrentUser(Authentication authentication, @Validated(OnUpdate.class) @RequestBody User updateUser){
        String email = authentication.getName();
        logger.info("Updating user info for: {}", email);
        try {
            userService.updateUserByUserEmail(email, updateUser);

            return ResponseEntity.ok("User updated successfully!");
        } catch (Exception e) {
            logger.error("Error updating user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    /**
     * Endpoint to delete the current user's account.
     *
     * @param authentication The authentication object.
     * @return ResponseEntity with success or error message.
     */
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
