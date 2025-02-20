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

    @Test
    void findByUser_ShouldReturnConnections_WhenUserHasConnections() {
        User user = createUser("user@example.com", "user123");
        User friend = createUser("friend@example.com", "friend123");

        entityManager.persist(user);
        entityManager.persist(friend);

        Connections connection = new Connections();
        connection.setUser(user);
        connection.setFriend(friend);
        connection.setUserId(user.getId());
        connection.setConnectionId(friend.getId());

        entityManager.persist(connection);
        entityManager.flush();

        var connections = connectionsRepository.findByUser(user);

        assertThat(connections).hasSize(1);
        assertThat(connections.get(0).getFriend().getEmail()).isEqualTo("friend@example.com");
    }

    @Test
    void findByUserAndFriend_ShouldReturnConnection_WhenConnectionExists() {
        User user = createUser("user@example.com", "user123");
        User friend = createUser("friend@example.com", "friend123");

        entityManager.persist(user);
        entityManager.persist(friend);

        Connections connection = new Connections();
        connection.setUser(user);
        connection.setFriend(friend);
        connection.setUserId(user.getId());
        connection.setConnectionId(friend.getId());

        entityManager.persist(connection);
        entityManager.flush();

        Optional<Connections> foundConnection = connectionsRepository.findByUserAndFriend(user, friend);

        assertThat(foundConnection).isPresent();
        assertThat(foundConnection.get().getFriend().getEmail()).isEqualTo("friend@example.com");
    }

    @Test
    void existsById_ShouldReturnTrue_WhenConnectionExists() {
        User user = createUser("user@example.com", "user123");
        User friend = createUser("friend@example.com", "friend123");

        entityManager.persist(user);
        entityManager.persist(friend);

        Connections connection = new Connections();
        connection.setUser(user);
        connection.setFriend(friend);
        connection.setUserId(user.getId());
        connection.setConnectionId(friend.getId());

        entityManager.persist(connection);
        entityManager.flush();

        ConnectionId connectionId = new ConnectionId(user.getId(), friend.getId());
        boolean exists = connectionsRepository.existsById(connectionId);

        assertThat(exists).isTrue();
    }

    @Test
    void existsById_ShouldReturnFalse_WhenConnectionDoesNotExist() {
        // Arrange : Créer 2 utilisateurs, mais ne pas créer la connexion entre eux
        User user = createUser("user@example.com", "user123");
        User friend = createUser("friend@example.com", "friend123");

        entityManager.persist(user);
        entityManager.persist(friend);
        entityManager.flush();

        // Ne pas créer la connexion, juste vérifier l'absence
        ConnectionId connectionId = new ConnectionId(user.getId(), friend.getId());

        // Act
        boolean exists = connectionsRepository.existsById(connectionId);

        // Assert
        assertThat(exists).isFalse();
    }


    private User createUser(String email, String username) {
        User user = new User();
        user.setEmail(email);
        user.setPassword("password123");
        user.setUsername(username);
        return user;
    }
}

