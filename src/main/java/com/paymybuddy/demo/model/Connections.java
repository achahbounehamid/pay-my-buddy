package com.paymybuddy.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "connections")
@IdClass(ConnectionId.class)
public class Connections {

    @Id
    @Column(name = "user_id")
    private int userId;

    @Id
    @Column(name = "connection_id")
    private int connectionId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "connection_id", insertable = false, updatable = false)
    private User friend;

    /**
     * Default constructor required by JPA.
     */
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }
}
