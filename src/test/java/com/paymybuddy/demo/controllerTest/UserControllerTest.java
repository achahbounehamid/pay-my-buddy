//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//import com.paymybuddy.demo.controller.rest.UserController;
//import com.paymybuddy.demo.service.
//import com.paymybuddy.demo.model.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//
//@WebMvcTest(UserController.class)
//public class UserControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    @WithMockUser(username = "testuser@example.com")
//    public void testRegisterUser() throws Exception {
//        User user = new User();
//        user.setUsername("testuser");
//        user.setPassword("password");
//        user.setEmail("testuser@example.com");
//
//        mockMvc.perform(post("/api/users/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"username\":\"testuser\",\"password\":\"password\",\"email\":\"testuser@example.com\"}"))
//                .andExpect(status().isCreated())
//                .andExpect(content().string("User registered successfully!"));
//
//        verify(userService, times(1)).registerUser(any(User.class));
//    }
//
//    @Test
//    public void testGetUserByUsername() throws Exception {
//        User user = new User();
//        user.setUsername("testuser");
//
//        when(userService.findUserByUsername("testuser")).thenReturn(user);
//
//        mockMvc.perform(get("/api/users/username/testuser"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value("testuser"));
//
//        verify(userService, times(1)).findUserByUsername("testuser");
//    }
//
//    @Test
//    @WithMockUser(username = "testuser@example.com")
//    public void testGetCurrentUser() throws Exception {
//        User user = new User();
//        user.setEmail("testuser@example.com");
//
//        when(userService.findUserByEmail("testuser@example.com")).thenReturn(user);
//
//        mockMvc.perform(get("/api/users/me"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.email").value("testuser@example.com"));
//
//        verify(userService, times(1)).findUserByEmail("testuser@example.com");
//    }
//}
