package com.paymybuddy.demo.controller;

import com.paymybuddy.demo.model.Transaction;
import com.paymybuddy.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public Transaction createTransaction(@RequestBody Transaction transaction){
        return  transactionService.createTransaction(
                transaction.getSende(),
                transaction.getReceiver(),
                transaction.getAmount(),
                transaction.getDescription()
        );
    }
}
