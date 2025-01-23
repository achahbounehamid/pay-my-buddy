package com.paymybuddy.demo.service;

import com.paymybuddy.demo.model.Transaction;
import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    public User findUserByEmail(String email) {
        return userService.findUserByEmail(email);
    }

    public Transaction createTransaction(User sender, User receiver, double amount, String description) {
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(BigDecimal.valueOf(amount));
        transaction.setDescription(description);
        return transactionRepository.save(transaction);
    }
}

