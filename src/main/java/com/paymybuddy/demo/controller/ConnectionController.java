package com.paymybuddy.demo.controller;


import com.paymybuddy.demo.model.Connections;
import com.paymybuddy.demo.model.User;
import com.paymybuddy.demo.service.ConnectionService;
import com.paymybuddy.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/connections")
public class ConnectionController {
    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private UserService userService;

    @GetMapping("/{email}")
    public List<Connections> getConnections(@PathVariable String email) {
        User user = userService.findUserByEmail(email);
        return connectionService.getConnectionsForUser(user);
    }

    @PostMapping
    public Connections addConnection(@RequestParam String userEmail, @RequestParam String connectionEmail) {
        User user = userService.findUserByEmail(userEmail);
        User connection = userService.findUserByEmail(connectionEmail);
        return connectionService.addConnection(user, connection);
    }
}
