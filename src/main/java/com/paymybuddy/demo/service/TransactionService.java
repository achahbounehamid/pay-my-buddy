package com.paymybuddy.demo.service;

import com.paymybuddy.demo.model.Transaction;
import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.repository.TransactionRepository;
import com.paymybuddy.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    // Effectuer une transaction
    public void createTransaction(int senderId, int receiverId, BigDecimal amount, String description) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        //  l'ID de l'expéditeur et du destinataire
        logger.info("Sender ID: " + senderId + " Receiver ID: " + receiverId);

        //  les connexions de l'expéditeur
        logger.info("Sender's Connections: " + sender.getConnections());

        // Vérifier si l'expéditeur et le destinataire sont connectés
        if (!sender.getConnections().contains(receiver)) {
            throw new IllegalArgumentException("Sender and receiver must be connected");
        }

        // Initialisation explicite de la collection connections
        sender.getConnections().size();

        // Créer et sauvegarder la transaction
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setTransactionDate(LocalDateTime.now());

        transactionRepository.save(transaction);
    }


    // Récupérer les transactions envoyées par un utilisateur
    public List<Transaction> getSentTransactions(int userId) {
        return transactionRepository.findBySenderId(userId);
    }

    // Récupérer les transactions reçues par un utilisateur
    public List<Transaction> getReceivedTransactions(int userId) {
        return transactionRepository.findByReceiverId(userId);
    }
    // Supprimer une transaction
    public void deleteTransaction(int transactionId) {
        transactionRepository.deleteById(transactionId);
    }
}
