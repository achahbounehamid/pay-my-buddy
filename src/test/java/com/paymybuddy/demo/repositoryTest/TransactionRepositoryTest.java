package com.paymybuddy.demo.repositoryTest;

import com.paymybuddy.demo.model.Transaction;
import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findBySender_Id_ShouldReturnTransactions_WhenTransactionsExist() {
        // Arrange
        User sender = new User();
        sender.setEmail("sender@example.com");
        sender.setPassword("password123");
        sender.setUsername("senderUser");
        entityManager.persist(sender);

        User receiver = new User();
        receiver.setEmail("receiver@example.com");
        receiver.setPassword("password123");
        receiver.setUsername("receiverUser");
        entityManager.persist(receiver);

        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setDescription("Test Transaction");
        entityManager.persist(transaction);
        entityManager.flush();

        // Act
        List<Transaction> transactions = transactionRepository.findBySender_Id(sender.getId());

        // Assert
        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getDescription()).isEqualTo("Test Transaction");
    }

    @Test
    void findByReceiver_Id_ShouldReturnTransactions_WhenTransactionsExist() {
        // Arrange
        User sender = new User();
        sender.setEmail("sender@example.com");
        sender.setPassword("password123");
        sender.setUsername("senderUser");
        entityManager.persist(sender);

        User receiver = new User();
        receiver.setEmail("receiver@example.com");
        receiver.setPassword("password123");
        receiver.setUsername("receiverUser");
        entityManager.persist(receiver);

        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(new BigDecimal("150.00"));
        transaction.setDescription("Payment Received");
        entityManager.persist(transaction);
        entityManager.flush();

        // Act
        List<Transaction> transactions = transactionRepository.findByReceiver_Id(receiver.getId());

        // Assert
        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getAmount()).isEqualTo(new BigDecimal("150.00"));
    }

    @Test
    void findBySender_IdAndReceiver_Id_ShouldReturnTransactions_WhenTransactionsExist() {
        // Arrange
        User sender = new User();
        sender.setEmail("sender@example.com");
        sender.setPassword("password123");
        sender.setUsername("senderUser");
        entityManager.persist(sender);

        User receiver = new User();
        receiver.setEmail("receiver@example.com");
        receiver.setPassword("password123"); // AJOUTE ICI
        receiver.setUsername("receiverUser"); // AJOUTE ICI
        entityManager.persist(receiver);

        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(new BigDecimal("75.00"));
        transaction.setDescription("Lunch Payment");
        entityManager.persist(transaction);
        entityManager.flush();

        // Act
        List<Transaction> transactions = transactionRepository.findBySender_IdAndReceiver_Id(sender.getId(), receiver.getId());

        // Assert
        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getDescription()).isEqualTo("Lunch Payment");
    }

}

