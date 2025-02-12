package com.paymybuddy.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class TransferViewController {

    @GetMapping("/transfer")
    public String showTransferPage() {
        return "transfer";
    }
}
