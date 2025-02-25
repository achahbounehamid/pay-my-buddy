package com.paymybuddy.demo.repository;

import com.paymybuddy.demo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Repository interface for managing Transaction entities.
 * This interface extends JpaRepository to provide CRUD operations
 * and additional query methods for Transaction entities.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    /**
     * Finds all transactions sent by a specific user (by sender ID).
     *
     * @param senderId The ID of the sender.
     * @return A list of Transaction entities sent by the sender.
     */
    List<Transaction> findBySender_Id(int senderId);
    /**
     * Finds all transactions received by a specific user (by receiver ID).
     *
     * @param receiverId The ID of the receiver.
     * @return A list of Transaction entities received by the receiver.
     */
    List<Transaction> findByReceiver_Id(int receiverId);
    /**
     * Finds all transactions between a specific sender and receiver.
     *
     * @param senderId   The ID of the sender.
     * @param receiverId The ID of the receiver.
     * @return A list of Transaction entities between the sender and receiver.
     */
    List<Transaction> findBySender_IdAndReceiver_Id(int senderId, int receiverId);
}

