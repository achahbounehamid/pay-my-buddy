//package com.paymybuddy.demo.controller;
//
//import com.paymybuddy.demo.model.User;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class PageController {
//
//    @GetMapping("/login")
//    public String showLoginPage() {
//        return "login";
//    }
//
//    @GetMapping("/register")
//    public String showRegisterPage() {
//        return "register";
//    }
//
//    @GetMapping("/transfer")
//    public String showTransferPage() {
//        return "transfer";
//    }
//
//    @GetMapping("/add-connection")
//    public String showAddConnectionPage() {
//        return "add_connection";
//    }
//
//    @GetMapping("/profile")
//    public String showProfilePage(Model model) {
//        // Exemple d'ajout de données à afficher
//        model.addAttribute("user", new User());
//        return "profile";
//    }
//}
//
