package com.mursion.assignment.itresources.service;


import com.mursion.assignment.itresources.model.ItResource;
import com.mursion.assignment.itresources.model.ItResourceAssign;
import com.mursion.assignment.itresources.repository.ItResourceAssignRepository;
import com.mursion.assignment.itresources.repository.ItResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing IT resource assignments.
 */
@Service
public class ItResourceAssignService {

    private final ItResourceAssignRepository itResourceAssignRepository;
    private final ItResourceRepository itResourceRepository;

    @Autowired
    public ItResourceAssignService(ItResourceAssignRepository itResourceAssignRepository, ItResourceRepository itResourceRepository) {
        this.itResourceAssignRepository = itResourceAssignRepository;
        this.itResourceRepository = itResourceRepository;
    }

    /**
     * Saves a new assignment and updates the resource status.
     *
     * @param assignment The assignment to save.
     * @return The saved assignment.
     */
    @Transactional
    public ItResourceAssign saveAssignment(ItResourceAssign assignment) {
        ItResource resource = itResourceRepository.findById(assignment.getResource().getId())
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        if (resource.getStatus() != ItResource.ResourceStatus.NOT_ASSIGNED) {
            throw new RuntimeException("Resource is not available for assignment");
        }

        resource.setStatus(ItResource.ResourceStatus.ASSIGNED);
        itResourceRepository.save(resource);

        assignment.setStatus(ItResourceAssign.AssignStatus.NOT_RETURNED);
        return itResourceAssignRepository.save(assignment);
    }

    /**
     * Retrieves an assignment by its ID.
     *
     * @param id The ID of the assignment.
     * @return An optional containing the assignment if found.
     */
    public Optional<ItResourceAssign> getAssignmentById(UUID id) {
        return itResourceAssignRepository.findById(id);
    }

    /**
     * Retrieves all assignments.
     *
     * @return A list of all assignments.
     */
    public List<ItResourceAssign> getAllAssignments() {
        return itResourceAssignRepository.findAll();
    }

    /**
     * Searches for assignments based on resource ID and/or user ID.
     *
     * @param resourceId The ID of the resource (optional).
     * @param userId     The ID of the user (optional).
     * @return A list of matching assignments.
     */
    public List<ItResourceAssign> searchAssignments(UUID resourceId, UUID userId) {
        if (resourceId != null && userId != null) {
            return itResourceAssignRepository.findByResourceIdAndUserId(resourceId, userId);
        } else if (resourceId != null) {
            return itResourceAssignRepository.findByResourceId(resourceId);
        } else if (userId != null) {
            return itResourceAssignRepository.findByUserId(userId);
        } else {
            return getAllAssignments();
        }
    }

    /**
     * Updates an existing assignment.
     *
     * @param id         The ID of the assignment to update.
     * @param assignment The updated assignment.
     * @return The updated assignment.
     */
    @Transactional
    public ItResourceAssign updateAssignment(UUID id, ItResourceAssign assignment) {
        ItResourceAssign existingAssignment = itResourceAssignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        existingAssignment.setUserId(assignment.getUserId());
        existingAssignment.setStatus(assignment.getStatus());
        existingAssignment.setAssignedDate(assignment.getAssignedDate());
        existingAssignment.setReturnDate(assignment.getReturnDate());

        return itResourceAssignRepository.save(existingAssignment);
    }

    /**
     * Deletes an assignment by its ID.
     *
     * @param id The ID of the assignment to delete.
     */
    @Transactional
    public void deleteAssignment(UUID id) {
        itResourceAssignRepository.deleteById(id);
    }

    /**
     * Marks an assigned resource as returned and updates the resource status.
     *
     * @param id The ID of the assignment.
     * @return The updated assignment.
     */
    @Transactional
    public ItResourceAssign returnResource(UUID id) {
        ItResourceAssign assignment = itResourceAssignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (assignment.getStatus() == ItResourceAssign.AssignStatus.RETURNED) {
            throw new RuntimeException("Resource is already returned");
        }

        assignment.setStatus(ItResourceAssign.AssignStatus.RETURNED);
        assignment.setReturnDate(LocalDate.now());

        ItResource resource = assignment.getResource();
        resource.setStatus(ItResource.ResourceStatus.NOT_ASSIGNED);
        itResourceRepository.save(resource);

        return itResourceAssignRepository.save(assignment);
    }
}