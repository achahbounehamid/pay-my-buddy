package com.paymybuddy.demo.controller;

import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.findUserByEmail(email);
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user) {
        System.out.println("Register endpoint called with user: " + user.getEmail());
        try {
            userService.registerUser(user);
            System.out.println("User registered successfully: " + user.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
        } catch (Exception e) {
            e.printStackTrace(); //  l'erreur complète
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }


    }
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return  ResponseEntity.ok(userService.findAllUsers());
    }
    @GetMapping("/{id}")
    public  ResponseEntity<User> getUserById(@PathVariable int id){
        User user =userService.findUserById(id);
        return  ResponseEntity.ok(user);
    }
    @PutMapping("/{id}")
    public  ResponseEntity<String> updateUser(@PathVariable int id, @Valid @RequestBody User updateUser){
        try {
            userService.updateUser(id, updateUser);
            return ResponseEntity.ok("User updated successfully");
        }catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public  ResponseEntity<String> deleteUser(@PathVariable int id){
        try{
            userService.deleteUser(id);
            return  ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return  ResponseEntity.status((HttpStatus.BAD_REQUEST)).body("Error:" + e.getMessage());
        }
    }
}
