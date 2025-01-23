package com.paymybuddy.demo.repository;

import com.paymybuddy.demo.model.Connections;
import com.paymybuddy.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionsRepository extends JpaRepository<Connections, Integer> {

    // Trouver les connexions d'un utilisateur
    List<Connections> findByUser(User user);

    // Vérifie si une connexion existe déjà entre deux utilisateurs
    boolean existsByUserAndConnection(User user, User connection);

    // Trouver les connexions d'un utilisateur par son id
    @Query("SELECT c.connection FROM Connections c WHERE c.user.id = :userId")
    List<User> findConnectionsByUserId(@Param("userId") int userId);
}
