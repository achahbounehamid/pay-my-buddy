package com.paymybuddy.demo.serviceTest;

import com.paymybuddy.demo.model.Transaction;
import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.repository.UserRepository;
import com.paymybuddy.demo.service.TransactionService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSendMoneySuccess() {
        // Arrange
        User sender = new User();
        sender.setEmail("sender@example.com");
        sender.setBalance(new BigDecimal("100.00"));
        userRepository.save(sender);

        User receiver = new User();
        receiver.setEmail("receiver@example.com");
        receiver.setBalance(new BigDecimal("50.00"));
        userRepository.save(receiver);

        BigDecimal amount = new BigDecimal("30.00");
        String description = "Payment for services";

        // Act
        Transaction transaction = transactionService.sendMoney(sender, receiver, amount, description);

        // Assert
        assertNotNull(transaction);
        assertEquals(sender.getBalance(), new BigDecimal("70.00"));
        assertEquals(receiver.getBalance(), new BigDecimal("80.00"));
        assertEquals(transaction.getDescription(), description);
    }
}

