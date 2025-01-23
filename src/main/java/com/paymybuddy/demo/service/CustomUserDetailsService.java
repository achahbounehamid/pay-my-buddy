//package com.paymybuddy.demo.service;
//
//import com.paymybuddy.demo.model.User;
//import com.paymybuddy.demo.repository.UserRepository;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class CustomUserDetailsService  implements UserDetailsService{
//    private final UserRepository userRepository;
//
//    public CustomUserDetailsService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Optional<User> optionalUser = userRepository.findByEmail(email);
//        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable : " + email));
//
//        return org.springframework.security.core.userdetails.User
//                .withUsername(user.getEmail())
//                .password(user.getPassword())
//                .roles("USER") // Vous pouvez étendre cela avec des rôles dynamiques
//                .build();
//    }
//
//}
