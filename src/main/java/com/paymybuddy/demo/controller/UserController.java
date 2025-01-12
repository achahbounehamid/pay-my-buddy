package com.paymybuddy.demo.controller;

import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> resgisterUser(@RequestBody User user) {
        User savedUser = userService.registerUser(user);
        return  ResponseEntity.ok(savedUser);
    }
    @GetMapping("/{email}")
    public  User getUserByEmail(@PathVariable String email){
        return  userService.findByEmail(email);
    }
}
