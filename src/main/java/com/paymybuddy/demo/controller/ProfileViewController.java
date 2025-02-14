package com.paymybuddy.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileViewController {

    @GetMapping("/profile")
    public String showProfilePage() {
        return "profile";
    }
}