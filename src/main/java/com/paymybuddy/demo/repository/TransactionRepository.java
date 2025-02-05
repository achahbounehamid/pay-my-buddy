package com.paymybuddy.demo.repository;

import com.paymybuddy.demo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Trouver les transactions envoyées par un utilisateur (par ID d'expéditeur)
    List<Transaction> findBySenderId(int senderId);

    // Trouver les transactions reçues par un utilisateur (par ID de destinataire)
    List<Transaction> findByReceiverId(int receiverId);

    // Trouver toutes les transactions entre un expéditeur et un destinataire
    List<Transaction> findBySenderIdAndReceiverId(int senderId, int receiverId);
}

