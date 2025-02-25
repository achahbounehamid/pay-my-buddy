package com.paymybuddy.demo.repository;

import com.paymybuddy.demo.model.ConnectionId;
import com.paymybuddy.demo.model.Connections;
import com.paymybuddy.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
/**
 * Repository interface for managing Connections entities.
 * This interface extends JpaRepository to provide CRUD operations
 * and additional query methods for Connections entities.
 */
@Repository
public interface ConnectionsRepository extends JpaRepository<Connections, ConnectionId> {
    /**
     * Finds all connections associated with a specific user.
     *
     * @param user The user whose connections are to be retrieved.
     * @return A list of Connections entities associated with the user.
     */
    List<Connections> findByUser(User user);
    /**
     * Checks if a connection with the specified ID exists.
     *
     * @param  * id * The composite ID of the connection to check.
     * @return True if the connection exists, otherwise false.
     */
    Optional<Connections> findByUserAndFriend(User user, User friend);

    boolean existsById(ConnectionId id);
}


