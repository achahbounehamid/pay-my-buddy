package com.paymybuddy.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.paymybuddy.demo.validation.OnCreate;
import com.paymybuddy.demo.validation.OnUpdate;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    @NotBlank(groups = {OnCreate.class, OnUpdate.class},message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @Column(nullable = false, unique = true)
    @NotBlank(groups = {OnCreate.class, OnUpdate.class},message = "Email cannot be blank")
    @Email(groups = {OnCreate.class, OnUpdate.class},message = "Email should be valid")
    private String email;

    @Column(nullable = false)
    @NotBlank(groups = OnCreate.class,message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = Set.of("ROLE_USER");

    // Relations
    @OneToMany(mappedBy = "sender")
    @JsonIgnore
    private Set<Transaction> sentTransactions;

    @OneToMany(mappedBy = "receiver")
    @JsonIgnore
    private Set<Transaction> receivedTransactions;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "connections",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "connection_id")
    )
    @JsonIgnore
    private Set<User> connections;
    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<Transaction> getSentTransactions() {
        return sentTransactions;
    }

    public void setSentTransactions(Set<Transaction> sentTransactions) {
        this.sentTransactions = sentTransactions;
    }

    public Set<Transaction> getReceivedTransactions() {
        return receivedTransactions;
    }

    public void setReceivedTransactions(Set<Transaction> receivedTransactions) {
        this.receivedTransactions = receivedTransactions;
    }

    public Set<User> getConnections() {
        return connections;
    }

    public void setConnections(Set<User> connections) {
        this.connections = connections;
    }
}
