package com.paymybuddy.demo.service;

import com.paymybuddy.demo.model.Connections;
import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.repository.ConnectionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectionService {
    @Autowired
    private ConnectionsRepository connectionRepository;

    public List<Connections> getConnectionsForUser(User user) {
        return connectionRepository.findByUser(user);
    }

    public Connections addConnection(User user, User connection) {
        Connections newConnection = new Connections();
        newConnection.setUser(user);
        newConnection.setConnection(connection);
        return connectionRepository.save(newConnection);
    }
}
