package com.paymybuddy.demo.controller.rest;

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
    /**
     * Constructor to initialize the TransactionService.
     *
     * @param transactionService Service for managing transactions.
     */
    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    /**
     * Endpoint to create a transaction.
     *
     * @param senderId     ID of the sender.
     * @param receiverId   ID of the receiver.
     * @param amount       Amount to be transferred.
     * @param description  Description of the transaction.
     * @return ResponseEntity with success or error message.
     */

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
/**
 * Endpoint to retrieve transactions sent by a user.
 *
 * @param userId ID of the user.
 * @return List of transactions sent by the user.
 */
    @GetMapping("/sent")
    public List<Transaction> getSentTransactions(@RequestParam int  userId) {
        return transactionService.getSentTransactions(userId);
    }

    /**
     * Endpoint to retrieve transactions received by a user.
     *
     * @param userId ID of the user.
     * @return List of transactions received by the user.
     */
    @GetMapping("/received")
    public List<Transaction> getReceivedTransactions(@RequestParam int userId) {
        return transactionService.getReceivedTransactions(userId);
    }
    /**
     * Endpoint to retrieve transactions between two users.
     *
     * @param senderId   ID of the sender.
     * @param receiverId ID of the receiver.
     * @return List of transactions between the two users.
     */
    @GetMapping("/between")
    public List<Transaction> getTransactionsBetweenUsers(@RequestParam int senderId, @RequestParam int receiverId) {
        return transactionService.getTransactionsBetweenUsers(senderId, receiverId);
    }

    /**
     * Endpoint to delete a transaction.
     *
     * @param transactionId ID of the transaction to delete.
     * @return ResponseEntity with success message.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteTransaction(@RequestParam int transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.ok("Transaction deleted successfully!");
    }

}
