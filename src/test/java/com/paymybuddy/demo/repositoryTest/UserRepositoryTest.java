package com.paymybuddy.demo.repositoryTest;

import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Optional;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByUsername_ShouldReturnUser_WhenUserExists() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        entityManager.persist(user);
        entityManager.flush();

        Optional<User> foundUser = userRepository.findByUsername("testUser");

        assertThat(foundUser)
                .isPresent()
                .hasValueSatisfying(u -> assertThat(u.getUsername()).isEqualTo("testUser"));
    }

    @Test
    void findByIdIn_ShouldReturnUsers_WhenIdsExist() {
        User user1 = new User();
        user1.setUsername("user1");
        user1.setEmail("user1@example.com");
        user1.setPassword("password");

        User user2 = new User();
        user2.setUsername("user2");
        user2.setEmail("user2@example.com");
        user2.setPassword("password");

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();

        List<User> users = userRepository.findByIdIn(List.of(user1.getId(), user2.getId()));

        assertThat(users).hasSize(2);
    }

    @Test
    void existsByUsername_ShouldReturnTrue_WhenUserExists() {
        User user = new User();
        user.setUsername("existingUser");
        user.setEmail("existing@example.com");
        user.setPassword("password");
        entityManager.persist(user);
        entityManager.flush();

        boolean exists = userRepository.existsByUsername("existingUser");

        assertThat(exists).isTrue();
    }

    @Test
    void existsByUsername_ShouldReturnFalse_WhenUserDoesNotExist() {
        boolean exists = userRepository.existsByUsername("nonExistingUser");

        assertThat(exists).isFalse();
    }

    @Test
    void findByUsername_ShouldReturnEmpty_WhenUserDoesNotExist() {
        Optional<User> user = userRepository.findByUsername("unknownUser");

        assertThat(user).isEmpty();
    }

    @Test
    void findByIdIn_ShouldReturnEmptyList_WhenIdsDoNotExist() {
        List<User> users = userRepository.findByIdIn(List.of(999, 1000));

        assertThat(users).isEmpty();
    }
}
