//package com.paymybuddy.demo.controller;
//
//import com.paymybuddy.demo.model.Connections;
//import com.paymybuddy.demo.model.User;
//import com.paymybuddy.demo.service.ConnectionService;
//import com.paymybuddy.demo.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/connections")
//public class ConnectionController {
//
//    @Autowired
//    private ConnectionService connectionService;
//
//    @Autowired
//    private UserService userService;
//
//    // Récupérer les connexions d'un utilisateur par son email
//    @GetMapping("/{email}")
//    public ResponseEntity<List<Connections>> getConnections(@PathVariable String email) {
//        // Trouver l'utilisateur par email
//        User user = userService.findUserByEmail(email);
//        if (user == null) {
//            return ResponseEntity.badRequest().body(null);
//        }
//
//        // Récupérer et retourner les connexions de l'utilisateur
//        List<Connections> connections = connectionService.getConnectionsForUser(user);
//        return ResponseEntity.ok(connections);
//    }
//
//    // Ajouter une connexion entre deux utilisateurs
//    @PostMapping
//    public ResponseEntity<Connections> addConnection(@RequestParam String userEmail, @RequestParam String connectionEmail) {
//        User user = userService.findUserByEmail(userEmail);
//        User connection = userService.findUserByEmail(connectionEmail);
//
//        if (user == null || connection == null) {
//            return ResponseEntity.badRequest().body(null);
//        }
//
//        Connections newConnection = connectionService.addConnection(user, connection);
//        return ResponseEntity.ok(newConnection);
//    }
//}
