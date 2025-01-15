package com.paymybuddy.demo.service;

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
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Transaction> getTransactionsBySender(User sender) {
        return transactionRepository.findBySender(sender);
    }

    public List<Transaction> getTransactionsByReceiver(User receiver) {
        return transactionRepository.findByReceiver(receiver);
    }

    public Transaction sendMoney(User sender, User receiver, BigDecimal amount, String description) {
        // Vérifier si le solde du sender est suffisant
        if (sender.getBalance().compareTo(amount) < 0) { // Utiliser compareTo pour BigDecimal
            throw new RuntimeException("Insufficient balance");
        }

        // Mettre à jour les soldes en utilisant les méthodes de BigDecimal
        sender.setBalance(sender.getBalance().subtract(amount)); // Subtract pour déduire le montant
        receiver.setBalance(receiver.getBalance().add(amount));  // Add pour ajouter le montant

        // Sauvegarder les modifications des utilisateurs
        userRepository.save(sender);
        userRepository.save(receiver);

        // Créer la transaction
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(amount);
        transaction.setDescription(description);

        return transactionRepository.save(transaction);
    }
}
