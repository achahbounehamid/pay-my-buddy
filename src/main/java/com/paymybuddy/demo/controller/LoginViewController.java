package com.paymybuddy.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginViewController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Renvoie la page login.html depuis src/main/resources/templates/
    }
}