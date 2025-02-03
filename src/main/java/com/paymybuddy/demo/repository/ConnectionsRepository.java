package com.paymybuddy.demo.repository;

import com.paymybuddy.demo.model.ConnectionId;
import com.paymybuddy.demo.model.Connections;
import com.paymybuddy.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionsRepository extends JpaRepository<Connections, ConnectionId> {

    List<Connections> findByUser(User user);

    Optional<Connections> findByUserAndFriend(User user, User friend);

    boolean existsById(ConnectionId id);
}


