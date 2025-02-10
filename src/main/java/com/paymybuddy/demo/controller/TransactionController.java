package com.paymybuddy.demo.controller;

import com.paymybuddy.demo.model.Transaction;
import com.paymybuddy.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Endpoint pour créer une transaction
    @PostMapping("/create")
    public ResponseEntity<String> createTransaction(@RequestParam int senderId,
                                                    @RequestParam int receiverId,
                                                    @RequestParam BigDecimal amount,
                                                    @RequestParam String description) {
        try {
            transactionService.createTransaction(senderId, receiverId, amount, description);
            return ResponseEntity.ok("Transaction created successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint pour récupérer les transactions envoyées par un utilisateur
    @GetMapping("/sent")
    public List<Transaction> getSentTransactions(@RequestParam int userId) {
        return transactionService.getSentTransactions(userId);
    }

    // Endpoint pour récupérer les transactions reçues par un utilisateur
    @GetMapping("/received")
    public List<Transaction> getReceivedTransactions(@RequestParam int userId) {
        return transactionService.getReceivedTransactions(userId);
    }
    // Endpoint pour récupérer les transactions entre un expéditeur et un destinataire
    @GetMapping("/between")
    public List<Transaction> getTransactionsBetweenUsers(@RequestParam int senderId, @RequestParam int receiverId) {
        return transactionService.getTransactionsBetweenUsers(senderId, receiverId);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteTransaction(@RequestParam int transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.ok("Transaction deleted successfully!");
    }

}
