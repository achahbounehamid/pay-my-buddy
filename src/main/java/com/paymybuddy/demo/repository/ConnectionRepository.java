package com.paymybuddy.demo.repository;

import com.paymybuddy.demo.model.Connection;
import com.paymybuddy.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    List<Connection> findByUser(User user);

}
