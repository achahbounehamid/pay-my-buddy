//package com.paymybuddy.demo.SecurityIntegration;
//
//import com.paymybuddy.demo.filter.JWTFilter;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//class SecurityIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private JWTFilter jwtFilter; // Remplace le filtre JWT dans le contexte
//
//    @Test
//    void shouldAllowAccessToPublicEndpoint_Login() throws Exception {
//        mockMvc.perform(get("/login"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void shouldAllowAccessToPublicEndpoint_Register() throws Exception {
//        mockMvc.perform(get("/register"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void shouldAllowAccessToApiUsersWithoutAuthentication() throws Exception {
//        mockMvc.perform(get("/api/users/login"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void shouldDenyAccessToProtectedApiWithoutToken() throws Exception {
//        mockMvc.perform(get("/api/connections"))
//                .andExpect(status().isUnauthorized());
//    }
//}
//
//
