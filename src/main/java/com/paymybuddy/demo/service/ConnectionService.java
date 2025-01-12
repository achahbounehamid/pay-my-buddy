package com.paymybuddy.demo.service;

import com.paymybuddy.demo.model.Connection;
import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.repository.ConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConnectionService {

    @Autowired
    private ConnectionRepository connectionRepository;

    public List<Connection> findConnectionsForUser(User user) {
        return  connectionRepository.findByUser(user);
    }
}
