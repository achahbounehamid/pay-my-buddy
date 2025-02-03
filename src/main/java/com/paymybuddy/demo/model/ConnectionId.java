package com.paymybuddy.demo.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ConnectionId implements Serializable {
    private int userId;
    private int connectionId;

    public ConnectionId() {
    }

    public ConnectionId(int userId, int connectionId) {
        this.userId = userId;
        this.connectionId = connectionId;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectionId that = (ConnectionId) o;
        return userId == that.userId && connectionId == that.connectionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, connectionId);
    }
}
