package com.paymybuddy.demo.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class AddConnectionViewController {

    @GetMapping("/addConnection")
    public String showAddConnectionPage() {
        return "addConnection";
    }
}
