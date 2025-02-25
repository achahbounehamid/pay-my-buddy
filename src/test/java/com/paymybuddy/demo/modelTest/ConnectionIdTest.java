package com.paymybuddy.demo.modelTest;

import com.paymybuddy.demo.model.ConnectionId;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConnectionIdTest {

    @Test
    void testConstructors() {
        // Test the default constructor
        ConnectionId connectionId1 = new ConnectionId();
        // Test the parameterized constructor
        ConnectionId connectionId2 = new ConnectionId(1, 2);
        // Verify default values are initialized correctly
        assertEquals(0, connectionId1.getUserId());
        assertEquals(0, connectionId1.getConnectionId());

        // Verify parameterized values are initialized correctly
        assertEquals(1, connectionId2.getUserId());
        assertEquals(2, connectionId2.getConnectionId());
    }

    @Test
    void testGettersAndSetters() {
        ConnectionId connectionId = new ConnectionId();

        // Set values using setters
        connectionId.setUserId(10);
        connectionId.setConnectionId(20);

        // Verify values are set and retrieved correctly
        assertEquals(10, connectionId.getUserId());
        assertEquals(20, connectionId.getConnectionId());
    }

    @Test
    void testEquals() {
        ConnectionId connectionId1 = new ConnectionId(1, 2);
        ConnectionId connectionId2 = new ConnectionId(1, 2);
        ConnectionId connectionId3 = new ConnectionId(2, 3);

        // Verify that equal objects are recognized as equal
        assertEquals(connectionId1, connectionId2);
        // Verify that different objects are not considered equal
        assertNotEquals(connectionId1, connectionId3);
    }

    @Test
    void testHashCode() {
        ConnectionId connectionId1 = new ConnectionId(1, 2);
        ConnectionId connectionId2 = new ConnectionId(1, 2);
        ConnectionId connectionId3 = new ConnectionId(2, 3);

        // Verify that equal objects have the same hash code
        assertEquals(connectionId1.hashCode(), connectionId2.hashCode());
        // Verify that different objects have different hash codes
        assertNotEquals(connectionId1.hashCode(), connectionId3.hashCode());
    }
}
