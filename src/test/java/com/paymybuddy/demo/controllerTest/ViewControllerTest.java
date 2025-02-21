package com.paymybuddy.demo.controllerTest;

import com.paymybuddy.demo.controller.view.*;
import com.paymybuddy.demo.filter.JWTFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest({AddConnectionViewController.class, LoginViewController.class, ProfileViewController.class, RegisterViewController.class, TransferViewController.class})
@AutoConfigureMockMvc(addFilters = false)
class ViewControllersTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JWTFilter jwtFilter;

    @Test
    void showAddConnectionPage_ShouldReturnAddConnectionView() throws Exception {
        mockMvc.perform(get("/addConnection"))
                .andExpect(status().isOk())
                .andExpect(view().name("addConnection"));
    }

    @Test
    void showLoginPage_ShouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void showProfilePage_ShouldReturnProfileView() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));
    }

    @Test
    void showRegisterPage_ShouldReturnRegisterView() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void showTransferPage_ShouldReturnTransferView() throws Exception {
        mockMvc.perform(get("/transfer"))
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"));
    }
}
