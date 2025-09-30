package com.example.demo.repository.entity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity // anotação/annotation
@Table(name = "tickets")
public class Ticket {

    public enum Status {
        TODO,
        DOING,
        DONE,
        CANCELED;

      public static Optional<Status> parse(String status) {
            if(status == null){
                return Optional.empty();
            }
            return Arrays.stream(Status.values())
                .filter(s -> s.name().equalsIgnoreCase(status))
                .findAny();
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = false, length = 255)
    private String details;

    @Column(nullable = false, unique = false, length = 255)
    private String action;

    @Column(nullable = false, unique = false, length = 255)
    private String local;

    @Column(nullable = false, unique = false, length = 255)
    private String object;

    @Column(name="cancel_reason",nullable = false, unique = false, length = 255)
    private String cancelReason;


    @Column(name = "created_at",nullable = false, unique = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at",nullable = false, unique = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;


    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "ticket_observers", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "ticket_id")
    )
    private Set<User> observers = new HashSet<>();

    public Ticket() {
        status = Status.TODO;
        var now = LocalDateTime.now();
        setUpdatedAt(now);
        setCreatedAt(now);
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
        this.observers.add(manager);
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
        this.observers.add(recipient);
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
        this.observers.add(owner);
    }

    public Set<User> getObservers() {
        return observers;
    }

    public void setObservers(Set<User> observers) {
        this.observers = observers;
    }

    public boolean isCanceled() {
        return Ticket.Status.CANCELED == this.status;
    }

}

