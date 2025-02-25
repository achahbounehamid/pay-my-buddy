package com.paymybuddy.demo.service;

import com.paymybuddy.demo.model.Connections;
import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.repository.ConnectionsRepository;
import com.paymybuddy.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing user connections.
 * This class provides methods to add, retrieve, and remove friends.
 */
@Service
public class ConnectionService {

    private final UserRepository userRepository;
    private final ConnectionsRepository connectionRepository;
    /**
     * Constructor to initialize repositories.
     *
     * @param userRepository        Repository for accessing User entities.
     * @param connectionRepository  Repository for accessing Connections entities.
     */
    @Autowired
    public ConnectionService(UserRepository userRepository, ConnectionsRepository connectionRepository) {
        this.userRepository = userRepository;
        this.connectionRepository = connectionRepository;
    }
    /**
     * Checks if both users exist.
     *
     * @param user  The first user.
     * @param friend The second user.
     * @return True if both users exist, otherwise false.
     */
    private boolean checkIfUsersExist(User user, User friend) {
        return user != null && friend != null;
    }
    /**
     * Adds a friend to a user.
     *
     * @param username    The username of the user adding a friend.
     * @param friendEmail The email of the friend to be added.
     * @throws IllegalArgumentException if users do not exist or are already connected.
     */
    public void addFriend(String username, String friendEmail) {
        User user = userRepository.findByEmail(username)

                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        User friend = userRepository.findByEmail(friendEmail)
                .orElseThrow(() -> new IllegalArgumentException("Friend not found: " + friendEmail));

        // Check if the connection already exists
        if (connectionRepository.findByUserAndFriend(user, friend).isPresent()) {
            throw new IllegalArgumentException("Already connected with this friend.");

        }
        // Create a bidirectional connection
        Connections connection1 = new Connections();
        connection1.setUser(user);
        connection1.setFriend(friend);
        connection1.setUserId(user.getId());
        connection1.setConnectionId(friend.getId());
        connectionRepository.save(connection1);

        // Create the reverse connection
        Connections connection2 = new Connections();
        connection2.setUser(friend);
        connection2.setFriend(user);
        connection2.setUserId(friend.getId());
        connection2.setConnectionId(user.getId());
        connectionRepository.save(connection2);


    }

    /**
     * Retrieves a list of friends for a given user.
     *
     * @param username The username of the user whose friends are to be retrieved.
     * @return A list of User entities representing the user's friends.
     */
    public List<User> getFriends(String username) {

        User user = userRepository.findByEmail(username)

                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        // Force initialization of connections
        user.getConnections().size();
        return new ArrayList<>(user.getConnections());
    }

    /**
     * Removes a friend from a user's connections.
     *
     * @param email       The email of the user removing a friend.
     * @param friendEmail The email of the friend to be removed.
     * @throws IllegalArgumentException if users do not exist or are not connected.
     */
    public void removeFriend(String email, String friendEmail) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));

        User friend = userRepository.findByEmail(friendEmail)
                .orElseThrow(() -> new IllegalArgumentException("Friend not found: " + friendEmail));
// Find and delete the connection from user to friend
        Connections connection1 = connectionRepository.findByUserAndFriend(user, friend)
                .orElseThrow(() -> new IllegalArgumentException("Friend not found in user's connections."));

        Connections connection2 = connectionRepository.findByUserAndFriend(friend, user)
                .orElseThrow(() -> new IllegalArgumentException("Friend not found in friend's connections."));

        // Delete both connections
        connectionRepository.delete(connection1);
        connectionRepository.delete(connection2);
    }
}

