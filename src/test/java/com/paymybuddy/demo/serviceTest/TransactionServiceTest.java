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
    /**
     * Set up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    /**
     * Test that creating a transaction saves it when users are connected.
     */
    @Test
    void createTransaction_ShouldSaveTransaction_WhenUsersAreConnected() {
        // Arrange: Create mock users and their connections
        User sender = new User();
        sender.setId(1);
        User receiver = new User();
        receiver.setId(2);
        // Mock repository behavior
        when(userRepository.findById(1)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2)).thenReturn(Optional.of(receiver));
        when(connectionsRepository.findByUserAndFriend(sender, receiver)).thenReturn(Optional.of(new Connections()));
        when(connectionsRepository.findByUserAndFriend(receiver, sender)).thenReturn(Optional.of(new Connections()));
        // Act: Create a transaction
        transactionService.createTransaction(1, 2, BigDecimal.valueOf(50), "Payment for service");
         // Assert: Verify the transaction is saved
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
    /**
     * Test that creating a transaction throws an exception when users are not connected.
     */
    @Test
    void createTransaction_ShouldThrowException_WhenUsersAreNotConnected() {
        // Arrange: Create mock users without connections
        User sender = new User();
        sender.setId(1);
        User receiver = new User();
        receiver.setId(2);
        // Mock repository behavior
        when(userRepository.findById(1)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2)).thenReturn(Optional.of(receiver));
        when(connectionsRepository.findByUserAndFriend(sender, receiver)).thenReturn(Optional.empty());
        when(connectionsRepository.findByUserAndFriend(receiver, sender)).thenReturn(Optional.empty());
        // Act & Assert: Verify exception is thrown with the correct message
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                transactionService.createTransaction(1, 2, BigDecimal.valueOf(50), "Payment for service")
        );

        assertEquals("Sender and receiver must be connected", exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
    /**
     * Test that retrieving sent transactions returns the correct transactions.
     */
    @Test
    void getSentTransactions_ShouldReturnTransactions() {
        // Arrange: Create a mock transaction
        Transaction transaction = new Transaction();
        transaction.setId(1);
        // Mock repository behavior
        when(transactionRepository.findBySender_Id(1)).thenReturn(List.of(transaction));
         // Act: Retrieve sent transactions
        List<Transaction> result = transactionService.getSentTransactions(1);
        // Assert: Verify the correct transactions are returned
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }
    /**
     * Test that retrieving received transactions returns the correct transactions.
     */
    @Test
    void getReceivedTransactions_ShouldReturnTransactions() {
        // Arrange: Create a mock transaction
        Transaction transaction = new Transaction();
        transaction.setId(1);
        // Mock repository behavior
        when(transactionRepository.findByReceiver_Id(2)).thenReturn(List.of(transaction));
// Act: Retrieve received transactions
        List<Transaction> result = transactionService.getReceivedTransactions(2);
// Assert: Verify the correct transactions are returned
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }
    /**
     * Test that retrieving transactions between users returns the correct transactions.
     */
    @Test
    void getTransactionsBetweenUsers_ShouldReturnTransactions() {
        // Arrange: Create a mock transaction
        Transaction transaction = new Transaction();
        transaction.setId(1);
        // Mock repository behavior
        when(transactionRepository.findBySender_IdAndReceiver_Id(1, 2)).thenReturn(List.of(transaction));
// Act: Retrieve transactions between users
        List<Transaction> result = transactionService.getTransactionsBetweenUsers(1, 2);
// Assert: Verify the correct transactions are returned
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
    }
    /**
     * Test that deleting a transaction by ID deletes it correctly.
     */
    @Test
    void deleteTransaction_ShouldDeleteTransactionById() {
        // Act: Delete a transaction by ID
        transactionService.deleteTransaction(1);

        verify(transactionRepository, times(1)).deleteById(1);
    }
}

