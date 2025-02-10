package com.paymybuddy.demo.service;

import com.paymybuddy.demo.model.Connections;
import com.paymybuddy.demo.model.Transaction;
import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.repository.ConnectionsRepository;
import com.paymybuddy.demo.repository.TransactionRepository;
import com.paymybuddy.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final ConnectionsRepository connectionsRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository, ConnectionsRepository connectionsRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.connectionsRepository = connectionsRepository;
    }

    // Effectuer une transaction
    public void createTransaction(int senderId, int receiverId, BigDecimal amount, String description) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        //  l'ID de l'expéditeur et du destinataire
        logger.info("Sender ID: " + senderId + " Receiver ID: " + receiverId);

        // Vérifier si l'expéditeur et le destinataire sont connectés
        if (!areUsersConnected(sender, receiver)) {
            throw new IllegalArgumentException("Sender and receiver must be connected");
        }

        // Initialisation explicite de la collection connections
//        sender.getConnections().size();

        // Créer et sauvegarder la transaction
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setTransactionDate(LocalDateTime.now());

        transactionRepository.save(transaction);
    }
    // Vérifier si les utilisateurs sont connectés
    private boolean areUsersConnected(User sender, User receiver) {
        // Vérifier si une connexion existe entre l'expéditeur et le destinataire
        Optional<Connections> connection1 = connectionsRepository.findByUserAndFriend(sender, receiver);
        Optional<Connections> connection2 = connectionsRepository.findByUserAndFriend(receiver, sender);
        return connection1.isPresent() && connection2.isPresent();
    }

    // Récupérer les transactions envoyées par un utilisateur
    public List<Transaction> getSentTransactions(int userId) {
        return transactionRepository.findBySender_Id(userId);
    }

    // Récupérer les transactions reçues par un utilisateur
    public List<Transaction> getReceivedTransactions(int userId) {
        return transactionRepository.findByReceiver_Id(userId);
    }
    // Récupérer les transactions entre un expéditeur et un destinataire
    public List<Transaction> getTransactionsBetweenUsers(int senderId, int receiverId) {
        return transactionRepository.findBySender_IdAndReceiver_Id(senderId, receiverId);
    }
    // Supprimer une transaction
    public void deleteTransaction(int transactionId) {
        transactionRepository.deleteById(transactionId);
    }
}
