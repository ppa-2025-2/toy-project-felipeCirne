package com.example.demo.repository.entity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "tickets")
public class Ticket {

    public enum Status {
        TODO, DOING, DONE, CANCELED;

        public static Optional<Status> parse(String status) {
            if (status == null) {
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

    @Column(nullable = false)
    private String details;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String local;

    @Column(nullable = false)
    private String object;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "manager_id")
    private Integer managerId;

    @Column(name = "recipient_id")
    private Integer recipientId;

    @Column(name = "owner_id", nullable = false)
    private Integer ownerId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ticket_observers", joinColumns = @JoinColumn(name = "ticket_id"))
    @Column(name = "user_id")
    private Set<Integer> observerIds = new HashSet<>();

    public Ticket() {
        this.status = Status.TODO;
        var now = LocalDateTime.now();
        setUpdatedAt(now);
        setCreatedAt(now);
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
        if (managerId != null) {
            this.observerIds.add(managerId);
        }
    }

    public Integer getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
        if (recipientId != null) {
            this.observerIds.add(recipientId);
        }
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
        if (ownerId != null) {
            this.observerIds.add(ownerId);
        }
    }

    public Set<Integer> getObserverIds() {
        return observerIds;
    }

    public void setObserverIds(Set<Integer> observerIds) {
        this.observerIds = observerIds;
    }

    public boolean isCanceled() {
        return Ticket.Status.CANCELED == this.status;
    }
}
