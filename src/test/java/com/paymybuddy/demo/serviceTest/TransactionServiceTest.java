package com.paymybuddy.demo.serviceTest;

import com.paymybuddy.demo.model.Connections;
import com.paymybuddy.demo.model.Transaction;
import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.repository.ConnectionsRepository;
import com.paymybuddy.demo.repository.TransactionRepository;
import com.paymybuddy.demo.repository.UserRepository;
import com.paymybuddy.demo.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConnectionsRepository connectionsRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTransaction_ShouldSaveTransaction_WhenUsersAreConnected() {
        User sender = new User();
        sender.setId(1);
        User receiver = new User();
        receiver.setId(2);

        when(userRepository.findById(1)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2)).thenReturn(Optional.of(receiver));
        when(connectionsRepository.findByUserAndFriend(sender, receiver)).thenReturn(Optional.of(new Connections()));
        when(connectionsRepository.findByUserAndFriend(receiver, sender)).thenReturn(Optional.of(new Connections()));

        transactionService.createTransaction(1, 2, BigDecimal.valueOf(50), "Payment for service");

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void createTransaction_ShouldThrowException_WhenUsersAreNotConnected() {
        User sender = new User();
        sender.setId(1);
        User receiver = new User();
        receiver.setId(2);

        when(userRepository.findById(1)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2)).thenReturn(Optional.of(receiver));
        when(connectionsRepository.findByUserAndFriend(sender, receiver)).thenReturn(Optional.empty());
        when(connectionsRepository.findByUserAndFriend(receiver, sender)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                transactionService.createTransaction(1, 2, BigDecimal.valueOf(50), "Payment for service")
        );

        assertEquals("Sender and receiver must be connected", exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void getSentTransactions_ShouldReturnTransactions() {
        Transaction transaction = new Transaction();
        transaction.setId(1);

        when(transactionRepository.findBySender_Id(1)).thenReturn(List.of(transaction));

        List<Transaction> result = transactionService.getSentTransactions(1);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void getReceivedTransactions_ShouldReturnTransactions() {
        Transaction transaction = new Transaction();
        transaction.setId(1);

        when(transactionRepository.findByReceiver_Id(2)).thenReturn(List.of(transaction));

        List<Transaction> result = transactionService.getReceivedTransactions(2);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void getTransactionsBetweenUsers_ShouldReturnTransactions() {
        Transaction transaction = new Transaction();
        transaction.setId(1);

        when(transactionRepository.findBySender_IdAndReceiver_Id(1, 2)).thenReturn(List.of(transaction));

        List<Transaction> result = transactionService.getTransactionsBetweenUsers(1, 2);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }

    @Test
    void deleteTransaction_ShouldDeleteTransactionById() {
        transactionService.deleteTransaction(1);

        verify(transactionRepository, times(1)).deleteById(1);
    }
}

