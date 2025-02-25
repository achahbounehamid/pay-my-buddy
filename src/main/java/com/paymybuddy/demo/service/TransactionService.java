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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
/**
 * Service class for managing transactions between users.
 * This class provides methods to create, retrieve, and delete transactions.
 */
@Service
public class TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final ConnectionsRepository connectionsRepository;
    /**
     * Constructor to initialize repositories.
     *
     * @param transactionRepository   Repository for accessing Transaction entities.
     * @param userRepository          Repository for accessing User entities.
     * @param connectionsRepository    Repository for accessing Connections entities.
     */
    @Autowired
    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository, ConnectionsRepository connectionsRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.connectionsRepository = connectionsRepository;
    }
    /**
     * Creates a new transaction between a sender and a receiver.
     *
     * @param senderId    The ID of the sender.
     * @param receiverId  The ID of the receiver.
     * @param amount      The amount to be transferred.
     * @param description The description of the transaction.
     * @throws IllegalArgumentException if sender or receiver does not exist or are not connected.
     */
    @Transactional
    public void createTransaction(int senderId, int receiverId, BigDecimal amount, String description) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        logger.info("Sender ID: " + senderId + " Receiver ID: " + receiverId);

        // Check if sender and receiver are connected
        if (!areUsersConnected(sender, receiver)) {
            throw new IllegalArgumentException("Sender and receiver must be connected");
        }

        // Create and save the transaction
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(amount);
        transaction.setDescription(description);
        transaction.setTransactionDate(LocalDateTime.now());

        transactionRepository.save(transaction);
    }
    /**
     * Checks if two users are connected.
     *
     * @param sender  The sender user.
     * @param receiver The receiver user.
     * @return True if both users are connected, otherwise false.
     */
    private boolean areUsersConnected(User sender, User receiver) {
        // Check if a connection exists between sender and receiver
        Optional<Connections> connection1 = connectionsRepository.findByUserAndFriend(sender, receiver);
        Optional<Connections> connection2 = connectionsRepository.findByUserAndFriend(receiver, sender);
        return connection1.isPresent() && connection2.isPresent();
    }

    /**
     * Retrieves transactions sent by a user.
     *
     * @param userId The ID of the user.
     * @return A list of Transaction entities sent by the user.
     */
    public List<Transaction> getSentTransactions(int userId) {
        return transactionRepository.findBySender_Id(userId);
    }

    /**
     * Retrieves transactions received by a user.
     *
     * @param userId The ID of the user.
     * @return A list of Transaction entities received by the user.
     */

    public List<Transaction> getReceivedTransactions(int userId) {
        return transactionRepository.findByReceiver_Id(userId);
    }
    /**
     * Retrieves transactions between two users.
     *
     * @param senderId   The ID of the sender.
     * @param receiverId The ID of the receiver.
     * @return A list of Transaction entities between the sender and receiver.
     */
    public List<Transaction> getTransactionsBetweenUsers(int senderId, int receiverId) {
        return transactionRepository.findBySender_IdAndReceiver_Id(senderId, receiverId);
    }
    /**
     * Deletes a transaction by its ID.
     *
     * @param *transactionId * The ID of the transaction to delete.
     */
    public void deleteTransaction(int transactionId) {
        transactionRepository.deleteById(transactionId);
    }
}
