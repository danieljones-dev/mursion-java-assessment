package com.mursion.assignment.itresources.model;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing an IT resource.
 */
@Data
@Entity
@Table(name = "it_resource", schema = "resources")
public class ItResource {

    /**
     * Unique identifier for the IT resource.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * Name of the IT resource.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Type of the IT resource (e.g., LAPTOP, MONITOR).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceType type;

    /**
     * Status of the IT resource (e.g., ASSIGNED, NOT_ASSIGNED).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceStatus status;

    /**
     * Serial number of the IT resource.
     */
    @Column(name = "serial_number", unique = true)
    private String serialNumber;

    /**
     * Inventory location of the IT resource.
     */
    @Column(name = "inventory_location")
    private String inventoryLocation;

    /**
     * Timestamp of when the IT resource was created.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp of when the IT resource was last updated.
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
     * Enum representing the type of IT resource.
     */
    public enum ResourceType {
        LAPTOP, CHAIR, MOUSE, MOBILE, KEYBOARD, MONITOR, DESKTOP, PRINTER, SCANNER, OTHER
    }

    /**
     * Enum representing the status of IT resource.
     */
    public enum ResourceStatus {
        ASSIGNED, NOT_ASSIGNED, DEPRECATED
    }
}