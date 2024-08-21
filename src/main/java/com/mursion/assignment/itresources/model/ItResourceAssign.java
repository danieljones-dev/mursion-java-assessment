package com.mursion.assignment.itresources.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing an assignment of an IT resource to a user.
 */
@Data
@Entity
@Table(name = "it_resource_assign", schema = "resources")
public class ItResourceAssign {

    /**
     * Unique identifier for the assignment.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * The IT resource being assigned.
     */
    @ManyToOne
    @JoinColumn(name = "resource_id", nullable = false)
    private ItResource resource;

    /**
     * The ID of the user the resource is assigned to.
     */
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    /**
     * Status of the assignment (e.g., RETURNED, NOT_RETURNED).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssignStatus status;

    /**
     * Date when the resource was assigned.
     */
    @Column(name = "assigned_date", nullable = false)
    private LocalDate assignedDate;

    /**
     * Date when the resource was returned (if applicable).
     */
    @Column(name = "return_date")
    private LocalDate returnDate;

    /**
     * Timestamp of when the assignment was created.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp of when the assignment was last updated.
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Sets the creation and update timestamps before persisting.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    /**
     * Sets the update timestamp before updating.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Enum representing the status of an assignment.
     */
    public enum AssignStatus {
        RETURNED, NOT_RETURNED
    }
}