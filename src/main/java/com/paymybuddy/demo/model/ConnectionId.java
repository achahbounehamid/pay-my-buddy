package com.paymybuddy.demo.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ConnectionId implements Serializable {
    private int userId;
    private int connectionId;
    /**
     * Default constructor required by JPA.
     */
    public ConnectionId() {
    }
    /**
     * Constructor to initialize the ConnectionId.
     *
     * @param userId       The ID of the user.
     * @param connectionId The ID of the connection.
     */
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
    /**
     * Overrides equals to compare ConnectionId objects.
     *
     * @param o The object to compare.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectionId that = (ConnectionId) o;
        return userId == that.userId && connectionId == that.connectionId;
    }
    /**
     * Overrides hashCode to generate a hash code for the ConnectionId object.
     *
     * @return The hash code value for the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId, connectionId);
    }
}
