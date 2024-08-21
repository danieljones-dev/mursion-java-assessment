package com.mursion.assignment.itresources.repository;

import com.mursion.assignment.itresources.model.ItResourceAssign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing `ItResourceAssign` entities.
 */
@Repository
public interface ItResourceAssignRepository extends JpaRepository<ItResourceAssign, UUID> {

    /**
     * Finds all assignments for a given resource ID.
     *
     * @param resourceId The ID of the resource.
     * @return A list of assignments for the resource.
     */
    List<ItResourceAssign> findByResourceId(UUID resourceId);

    /**
     * Finds all assignments for a given user ID.
     *
     * @param userId The ID of the user.
     * @return A list of assignments for the user.
     */
    List<ItResourceAssign> findByUserId(UUID userId);

    /**
     * Finds all assignments for a given resource ID and user ID.
     *
     * @param resourceId The ID of the resource.
     * @param userId     The ID of the user.
     * @return A list of assignments for the resource and user.
     */
    List<ItResourceAssign> findByResourceIdAndUserId(UUID resourceId, UUID userId);
}