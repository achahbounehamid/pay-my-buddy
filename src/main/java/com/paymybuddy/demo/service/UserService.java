package com.paymybuddy.demo.service;

import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        // Cryptage du mot de passe
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return  userRepository.save(user);
    }
    public  User findByEmail(String email){
        return  userRepository.findByEmail(email);
    }
}
