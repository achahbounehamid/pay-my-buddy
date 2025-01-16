package com.paymybuddy.demo.controllerTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.paymybuddy.demo.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.math.BigDecimal;
import java.util.Optional;
import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.repository.UserRepository;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    void testGetUserByEmail() throws Exception {
        // Arrange
        User user = new User();
        user.setEmail("bob@example.com");
        user.setBalance(new BigDecimal("100.00"));

        // Mockito doit renvoyer un Optional<User>
        when(userRepository.findByEmail("bob@example.com")).thenReturn(Optional.of(user));

        // Act & Assert
        mockMvc.perform(get("/api/users/bob@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("bob@example.com"))
                .andExpect(jsonPath("$.balance").value("100.00"));
    }
}

