package com.paymybuddy.demo.repository;

import com.paymybuddy.demo.model.Connections;
import com.paymybuddy.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ConnectionsRepository extends JpaRepository<Connections, Integer> {
    List<Connections> findByUser(User user);
}
