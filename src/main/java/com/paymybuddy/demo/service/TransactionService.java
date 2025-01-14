package com.paymybuddy.demo.service;

import com.paymybuddy.demo.model.Transaction;
import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction createTransaction(User sende, User receiver, BigDecimal amount, String description) {
        Transaction transaction = new Transaction();
        transaction.setSende(sende);
        transaction.setReceiver(receiver);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setTransactionDate(new Timestamp(System.currentTimeMillis()));

        return transactionRepository.save(transaction);
    }
}
