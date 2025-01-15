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

    @Autowired
    private UserService userService;

    @GetMapping("/sender/{email}")
    public List<Transaction> getTransactionsBySender(@PathVariable String email) {
        User sender = userService.findUserByEmail(email);
        return transactionService.getTransactionsBySender(sender);
    }

    @GetMapping("/receiver/{email}")
    public List<Transaction> getTransactionsByReceiver(@PathVariable String email) {
        User receiver = userService.findUserByEmail(email);
        return transactionService.getTransactionsByReceiver(receiver);
    }

    @PostMapping
    public Transaction sendMoney(
            @RequestParam String senderEmail,
            @RequestParam String receiverEmail,
            @RequestParam BigDecimal amount,
            @RequestParam String description) {
        User sender = userService.findUserByEmail(senderEmail);
        User receiver = userService.findUserByEmail(receiverEmail);
        return transactionService.sendMoney(sender, receiver, amount, description);
    }
}
