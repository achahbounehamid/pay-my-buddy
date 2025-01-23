package com.paymybuddy.demo.controller;

import com.paymybuddy.demo.model.Transaction;
import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.service.TransactionService;
import com.paymybuddy.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public Transaction createTransaction(
            @RequestParam String senderEmail,
            @RequestParam String receiverEmail,
            @RequestParam double amount,
            @RequestParam String description
    ) {
        User sender = transactionService.findUserByEmail(senderEmail);
        User receiver = transactionService.findUserByEmail(receiverEmail);
        return transactionService.createTransaction(sender, receiver, amount, description);
    }
}
