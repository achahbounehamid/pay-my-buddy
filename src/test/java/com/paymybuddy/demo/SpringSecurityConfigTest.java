//package com.paymybuddy.demo;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.paymybuddy.demo.filter.JWTFilter;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class SpringSecurityConfigTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper; // Pour parser les réponses JSON
//
//    private String authToken; // Stocke le token pour les tests
//
//    @BeforeEach
//    void setUp() throws Exception {
//        authToken = getAuthToken();
//    }
//
//    /**
//     * Méthode pour récupérer un token JWT valide en simulant une connexion.
//     *
//     * @return le token JWT sous forme de String
//     * @throws Exception si la requête échoue
//     */
//    private String getAuthToken() throws Exception {
//        String requestBody = """
//        {
//            "email": "bob@example.com",
//            "password": "hashed_password_2"
//        }
//        """;
//
//        MvcResult result = mockMvc.perform(post("/api/users/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // Extraire le token depuis la réponse JSON
//        String responseJson = result.getResponse().getContentAsString();
//        JsonNode jsonNode = objectMapper.readTree(responseJson);
//        return jsonNode.get("token").asText();
//    }
//    @Test
//    void testLoginRequest() throws Exception {
//        String requestBody = """
//    {
//         "email": "bob@example.com",
//            "password": "hashed_password_2"
//    }
//    """;
//
//        MvcResult result = mockMvc.perform(post("/api/users/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andReturn();
//
//        System.out.println("🔴 Status login: " + result.getResponse().getStatus());
//        System.out.println("🔴 Response body: " + result.getResponse().getContentAsString());
//
//        assertEquals(200, result.getResponse().getStatus()); // Vérifier si le login passe
//    }
//
//    /**
//     * Vérifie que les endpoints publics sont accessibles sans authentification.
//     */
//    @Test
//    void testPublicEndpointsAccess() throws Exception {
//        mockMvc.perform(get("/login"))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/register"))
//                .andExpect(status().isOk());
//
//        // Vérification avec logs pour voir si les routes sont bien publiques
//        MvcResult result = mockMvc.perform(get("/login")).andReturn();
//        System.out.println(" Status /login: " + result.getResponse().getStatus());
//        System.out.println(" Body /login: " + result.getResponse().getContentAsString());
//    }
//
//    /**
//     * Vérifie que les endpoints protégés nécessitent une authentification.
//     */
//    @Test
//    void testProtectedEndpointsAccessWithoutToken() throws Exception {
//        mockMvc.perform(get("/api/connections"))
//                .andExpect(status().isUnauthorized());
//
//        mockMvc.perform(get("/api/transfers"))
//                .andExpect(status().isUnauthorized());
//
//        mockMvc.perform(get("/api/transactions/1"))
//                .andExpect(status().isUnauthorized());
//    }
//
//    /**
//     * Vérifie que les endpoints protégés sont accessibles avec un JWT valide.
//     */
//    @Test
//    void testProtectedEndpointsAccessWithToken() throws Exception {
//        mockMvc.perform(get("/api/connections")
//                        .header("Authorization", "Bearer " + authToken))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/api/transfers")
//                        .header("Authorization", "Bearer " + authToken))
//                .andExpect(status().isOk());
//
//        mockMvc.perform(get("/api/transactions/1")
//                        .header("Authorization", "Bearer " + authToken))
//                .andExpect(status().isOk());
//    }
//
//    /**
//     * Vérifie que le filtre JWT est correctement injecté.
//     */
//    @Autowired
//    private JWTFilter jwtFilter;
//
//    @Test
//    void testJwtFilterConfiguration() {
//        assertNotNull(jwtFilter);
//    }
//
//    /**
//     * Vérifie que l'encodeur de mot de passe est correctement configuré.
//     */
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Test
//    void testPasswordEncoderConfiguration() {
//        assertNotNull(passwordEncoder);
//        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);
//    }
//
//    /**
//     * Vérifie que l'authentication manager est correctement configuré.
//     */
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Test
//    void testAuthenticationManagerConfiguration() {
//        assertNotNull(authenticationManager);
//    }
//}
