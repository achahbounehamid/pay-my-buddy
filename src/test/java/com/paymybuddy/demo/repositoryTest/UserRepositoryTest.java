//package com.paymybuddy.demo.repositoryTest;
//
//import com.paymybuddy.demo.model.User;
//import com.paymybuddy.demo.repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.test.context.ActiveProfiles;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import java.util.List;
//import java.util.Optional;
//
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@ActiveProfiles("test")
//class UserRepositoryTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private TestEntityManager entityManager;// EntityManager for database operations
//    /**
//     * Test that finding a user by username returns the correct user when the user exists.
//     */
//    @Test
//    void findByUsername_ShouldReturnUser_WhenUserExists() {
//        User user = new User();
//        user.setUsername("testUser");
//        user.setEmail("test@example.com");
//        user.setPassword("password");
//        entityManager.persist(user);
//        entityManager.flush();
//// Act: Retrieve the user by username
//        Optional<User> foundUser = userRepository.findByUsername("testUser");
//        // Assert: Verify the user is found and has the correct username
//        assertThat(foundUser)
//                .isPresent()
//                .hasValueSatisfying(u -> assertThat(u.getUsername()).isEqualTo("testUser"));
//    }
//    /**
//     * Test that finding users by a list of IDs returns the correct users when the IDs exist.
//     */
//    @Test
//    void findByIdIn_ShouldReturnUsers_WhenIdsExist() {
//        // Arrange: Create and persist two users
//        User user1 = new User();
//        user1.setUsername("user1");
//        user1.setEmail("user1@example.com");
//        user1.setPassword("password");
//
//        User user2 = new User();
//        user2.setUsername("user2");
//        user2.setEmail("user2@example.com");
//        user2.setPassword("password");
//
//        entityManager.persist(user1);
//        entityManager.persist(user2);
//        entityManager.flush();
//        // Act: Retrieve users by their IDs
//        List<User> users = userRepository.findByIdIn(List.of(user1.getId(), user2.getId()));
//// Assert: Verify the correct number of users are returned
//        assertThat(users).hasSize(2);
//    }
//    /**
//     * Test that checking for the existence of a user by username returns true when the user exists.
//     */
//    @Test
//    void existsByUsername_ShouldReturnTrue_WhenUserExists() {
//        // Arrange: Create and persist a user
//        User user = new User();
//        user.setUsername("existingUser");
//        user.setEmail("existing@example.com");
//        user.setPassword("password");
//        entityManager.persist(user);
//        entityManager.flush();
//        // Act: Check for the existence of the user by username
//        boolean exists = userRepository.existsByUsername("existingUser");
//        // Assert: Verify the user exists
//        assertThat(exists).isTrue();
//    }
//    /**
//     * Test that checking for the existence of a user by username returns false when the user does not exist.
//     */
//    @Test
//    void existsByUsername_ShouldReturnFalse_WhenUserDoesNotExist() {
//        // Act: Check for the existence of a non-existent user by username
//        boolean exists = userRepository.existsByUsername("nonExistingUser");
//// Assert: Verify the user does not exist
//        assertThat(exists).isFalse();
//    }
//    /**
//     * Test that finding a user by username returns empty when the user does not exist.
//     */
//    @Test
//    void findByUsername_ShouldReturnEmpty_WhenUserDoesNotExist() {
//        // Act: Retrieve a non-existent user by username
//        Optional<User> user = userRepository.findByUsername("unknownUser");
//        // Assert: Verify the result is empty
//        assertThat(user).isEmpty();
//    }
//    /**
//     * Test that finding users by a list of IDs returns an empty list when the IDs do not exist.
//     */
//    @Test
//    void findByIdIn_ShouldReturnEmptyList_WhenIdsDoNotExist() {
//        // Act: Retrieve users by non-existent IDs
//        List<User> users = userRepository.findByIdIn(List.of(999, 1000));
//        // Assert: Verify the result is an empty list
//        assertThat(users).isEmpty();
//    }
//}
