//package com.paymybuddy.demo.controller;
//
//import com.paymybuddy.demo.model.User;
//import com.paymybuddy.demo.service.UserService;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//
//@RestController
//@RequestMapping("/api/users")
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    @GetMapping("/{email}")
//    public User getUserByEmail(@PathVariable String email) {
//        return userService.findUserByEmail(email);
//    }
//    @PostMapping("/register")
//    public ResponseEntity<String> registerUser(@Valid @RequestBody User user) {
//        try {
//            userService.registerUser(user);
//            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
//        }
//    }
//}
