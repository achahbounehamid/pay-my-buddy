package com.paymybuddy.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class AddConnectionViewController {

    @GetMapping("/add-connection")
    public String showAddConnectionPage() {
        return "addConnection";
    }
}
