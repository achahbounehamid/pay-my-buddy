package com.paymybuddy.demo.repositoryTest;

import com.paymybuddy.demo.model.Connections;
import com.paymybuddy.demo.model.ConnectionId;
import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.repository.ConnectionsRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ConnectionsRepositoryTest {

    @Autowired
    private ConnectionsRepository connectionsRepository;

    @Autowired
    private EntityManager entityManager;
    /**
     * Test that finding connections by user returns the correct connections when they exist.
     */
    @Test
    void findByUser_ShouldReturnConnections_WhenUserHasConnections() {
        // Create and persist users
        User user = createUser("user@example.com", "user123");
        User friend = createUser("friend@example.com", "friend123");

        entityManager.persist(user);
        entityManager.persist(friend);
        // Create and persist a connection between the users
        Connections connection = new Connections();
        connection.setUser(user);
        connection.setFriend(friend);
        connection.setUserId(user.getId());
        connection.setConnectionId(friend.getId());

        entityManager.persist(connection);
        entityManager.flush();
// Retrieve connections for the user
        var connections = connectionsRepository.findByUser(user);
        // Verify the results
        assertThat(connections).hasSize(1);
        assertThat(connections.get(0).getFriend().getEmail()).isEqualTo("friend@example.com");
    }
    /**
     * Test that finding a connection by user and friend returns the correct connection when it exists.
     */
    @Test
    void findByUserAndFriend_ShouldReturnConnection_WhenConnectionExists() {
        // Create and persist users
        User user = createUser("user@example.com", "user123");
        User friend = createUser("friend@example.com", "friend123");

        entityManager.persist(user);
        entityManager.persist(friend);
        // Create and persist a connection between the users
        Connections connection = new Connections();
        connection.setUser(user);
        connection.setFriend(friend);
        connection.setUserId(user.getId());
        connection.setConnectionId(friend.getId());

        entityManager.persist(connection);
        entityManager.flush();
// Retrieve the connection
        Optional<Connections> foundConnection = connectionsRepository.findByUserAndFriend(user, friend);

        assertThat(foundConnection).isPresent();
        assertThat(foundConnection.get().getFriend().getEmail()).isEqualTo("friend@example.com");
    }
    /**
     * Test that checking for the existence of a connection by ID returns true when the connection exists.
     */
    @Test
    void existsById_ShouldReturnTrue_WhenConnectionExists() {
        // Create and persist users
        User user = createUser("user@example.com", "user123");
        User friend = createUser("friend@example.com", "friend123");

        entityManager.persist(user);
        entityManager.persist(friend);
        // Create and persist a connection between the users
        Connections connection = new Connections();
        connection.setUser(user);
        connection.setFriend(friend);
        connection.setUserId(user.getId());
        connection.setConnectionId(friend.getId());

        entityManager.persist(connection);
        entityManager.flush();
        // Check for the existence of the connection
        ConnectionId connectionId = new ConnectionId(user.getId(), friend.getId());
        boolean exists = connectionsRepository.existsById(connectionId);
// Verify the result
        assertThat(exists).isTrue();
    }
    /**
     * Test that checking for the existence of a connection by ID returns false when the connection does not exist.
     */
    @Test
    void existsById_ShouldReturnFalse_WhenConnectionDoesNotExist() {
        // Arrange : Créer 2 utilisateurs, mais ne pas créer la connexion entre eux
        User user = createUser("user@example.com", "user123");
        User friend = createUser("friend@example.com", "friend123");

        entityManager.persist(user);
        entityManager.persist(friend);
        entityManager.flush();

        // Do not create a connection, just check for its absence
        ConnectionId connectionId = new ConnectionId(user.getId(), friend.getId());

        // Check for the existence of the connection
        boolean exists = connectionsRepository.existsById(connectionId);

        // Verify the result
        assertThat(exists).isFalse();
    }

    /**
     * Helper method to create a user with the given email and username.
     *
     * @param email    The email of the user.
     * @param username The username of the user.
     * @return The created User object.
     */
    private User createUser(String email, String username) {
        User user = new User();
        user.setEmail(email);
        user.setPassword("password123");
        user.setUsername(username);
        return user;
    }
}

