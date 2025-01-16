package com.paymybuddy.demo.service;

import com.paymybuddy.demo.exception.InsufficientBalanceException;
import com.paymybuddy.demo.model.Transaction;
import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.repository.TransactionRepository;
import com.paymybuddy.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service

public class TransactionService {


    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public List<Transaction> getTransactionsBySender(User sender) {
        if (sender == null) {
            throw new IllegalArgumentException("Sender cannot be null");
        }
        return transactionRepository.findBySender(sender);
    }

    public List<Transaction> getTransactionsByReceiver(User receiver) {
        if (receiver == null) {
            throw new IllegalArgumentException("Receiver cannot be null");
        }
        return transactionRepository.findByReceiver(receiver);
    }

    public Transaction sendMoney(User sender, User receiver, BigDecimal amount, String description) {
        if (sender == null || receiver == null || amount == null) {
            throw new IllegalArgumentException("Sender, receiver, and amount cannot be null");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        // Vérification du solde
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        // Mise à jour des soldes
        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        // Sauvegarder les utilisateurs
        userRepository.save(sender);
        userRepository.save(receiver);

        // Créer et sauvegarder la transaction
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(amount);
        transaction.setDescription(description != null ? description : "No description provided");

        return transactionRepository.save(transaction);
    }
}

