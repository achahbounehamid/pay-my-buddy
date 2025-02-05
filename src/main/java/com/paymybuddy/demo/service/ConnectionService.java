package com.paymybuddy.demo.service;

import com.paymybuddy.demo.model.Connections;
import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.repository.ConnectionsRepository;
import com.paymybuddy.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class ConnectionService {

    private final UserRepository userRepository;
    private final ConnectionsRepository connectionRepository;

    @Autowired
    public ConnectionService(UserRepository userRepository, ConnectionsRepository connectionRepository) {
        this.userRepository = userRepository;
        this.connectionRepository = connectionRepository;
    }
    // Vérifier si les deux utilisateurs existent
    private boolean checkIfUsersExist(User user, User friend) {
        return user != null && friend != null;
    }
    // Ajouter un ami
    public void addFriend(String username, String friendEmail) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        User friend = userRepository.findByEmail(friendEmail)
                .orElseThrow(() -> new IllegalArgumentException("Friend not found: " + friendEmail));

        // Vérifier si la connexion existe déjà
        if (connectionRepository.findByUserAndFriend(user, friend).isPresent()) {
            throw new IllegalArgumentException("Already connected with this friend.");
        }

        // Créer une connexion bidirectionnelle
        Connections connection1 = new Connections();
        connection1.setUser(user);
        connection1.setFriend(friend);
        connectionRepository.save(connection1);

        // Créer l'autre connexion bidirectionnelle (de friend vers user)
        Connections connection2 = new Connections();
        connection2.setUser(friend);
        connection2.setFriend(user);
        connectionRepository.save(connection2);
    }

    // Récupérer les amis d'un utilisateur
    public List<User> getFriends(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        // Forcer l'initialisation des connexions
        user.getConnections().size();
        return new ArrayList<>(user.getConnections());
    }

    // Supprimer un ami
    public void removeFriend(String username, String friendEmail) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        User friend = userRepository.findByEmail(friendEmail)
                .orElseThrow(() -> new IllegalArgumentException("Friend not found: " + friendEmail));
// Trouver la connexion de user à friend
        Connections connection1 = connectionRepository.findByUserAndFriend(user, friend)
                .orElseThrow(() -> new IllegalArgumentException("Friend not found in user's connections."));

        Connections connection2 = connectionRepository.findByUserAndFriend(friend, user)
                .orElseThrow(() -> new IllegalArgumentException("Friend not found in friend's connections."));

        // Supprimer les deux connexions
        connectionRepository.delete(connection1);
        connectionRepository.delete(connection2);
    }
}

