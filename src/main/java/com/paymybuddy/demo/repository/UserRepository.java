package com.paymybuddy.demo.repository;

import com.paymybuddy.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Repository interface for managing User entities.
     * This interface extends JpaRepository to provide CRUD operations
     * and additional query methods for User entities.
     */
Optional<User> findByUsername(String username);
    /**
     * Finds a user by their email address.
     *
     * @param email The email address of the user to find.
     * @return An Optional containing the User entity if found, otherwise empty.
     */
Optional<User> findByEmail(  String email);

    /**
     * Checks if a user with the specified username exists.
     *
     * @param username The username to check.
     * @return True if a user with the specified username exists, otherwise false.
     */
    boolean existsByUsername(String username);
    /**
     * Checks if a user with the specified email address exists.
     *
     * @param email The email address to check.
     * @return True if a user with the specified email address exists, otherwise false.
     */
    boolean existsByEmail(String email);
    /**
     * Finds users by their IDs.
     *
     * @param ids A list of user IDs to search for.
     * @return A list of User entities matching the provided IDs.
     */
    List<User> findByIdIn(List<Integer> ids);
}
