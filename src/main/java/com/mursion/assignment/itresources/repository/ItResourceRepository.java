package com.mursion.assignment.itresources.repository;

import com.mursion.assignment.itresources.model.ItResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing `ItResource` entities.
 */
@Repository
public interface ItResourceRepository extends JpaRepository<ItResource, UUID> {

    /**
     * Finds all resources with a name containing the given string (case-insensitive).
     *
     * @param name The name to search for.
     * @return A list of resources with matching names.
     */
    List<ItResource> findByNameContainingIgnoreCase(String name);

    /**
     * Finds all resources with the given type.
     *
     * @param type The type to search for.
     * @return A list of resources with matching types.
     */
    List<ItResource> findByType(String type);

    /**
     * Finds all resources with a name containing the given string (case-insensitive) and the given type.
     *
     * @param name The name to search for.
     * @param type The type to search for.
     * @return A list of resources with matching names and types.
     */
    List<ItResource> findByNameContainingIgnoreCaseAndType(String name, String type);

    /**
     * Finds all resources matching the given specification and sorted according to the given sort.
     *
     * @param spec The specification to use for filtering.
     * @param sort The sort to use for ordering.
     * @return A list of resources matching the specification and sorted.
     */
    List<ItResource> findAll(Specification<ItResource> spec, Sort sort);
}