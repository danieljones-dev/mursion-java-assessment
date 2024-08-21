package com.mursion.assignment.itresources.service;

import com.mursion.assignment.itresources.dto.it_resource.ItResourceSearchDto;
import com.mursion.assignment.itresources.model.ItResource;
import com.mursion.assignment.itresources.repository.ItResourceRepository;
import com.mursion.assignment.itresources.types.enums.SortDirection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing IT resources.
 */
@Service
public class ItResourceService {

    private final ItResourceRepository itResourceRepository;

    @Autowired
    public ItResourceService(ItResourceRepository itResourceRepository) {
        this.itResourceRepository = itResourceRepository;
    }

    /**
     * Saves a new IT resource.
     *
     * @param resource The IT resource to save.
     * @return The saved IT resource.
     */
    @Transactional
    public ItResource saveResource(ItResource resource) {
        return itResourceRepository.save(resource);
    }

    /**
     * Retrieves an IT resource by its ID.
     *
     * @param id The ID of the IT resource.
     * @return An optional containing the IT resource if found.
     */
    public Optional<ItResource> getResourceById(UUID id) {
        return itResourceRepository.findById(id);
    }

    /**
     * Retrieves all IT resources.
     *
     * @return A list of all IT resources.
     */
    public List<ItResource> getAllResources() {
        return itResourceRepository.findAll();
    }

    /**
     * Updates an existing IT resource.
     *
     * @param id       The ID of the IT resource to update.
     * @param resource The updated IT resource.
     * @return The updated IT resource.
     */
    @Transactional
    public ItResource updateResource(UUID id, ItResource resource) {
        ItResource existingResource = itResourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        existingResource.setName(resource.getName());
        existingResource.setType(resource.getType());
        existingResource.setStatus(resource.getStatus());
        existingResource.setSerialNumber(resource.getSerialNumber());
        existingResource.setInventoryLocation(resource.getInventoryLocation());

        return itResourceRepository.save(existingResource);
    }

    /**
     * Deletes an IT resource by its ID.
     *
     * @param id The ID of the IT resource to delete.
     */
    @Transactional
    public void deleteResource(UUID id) {
        itResourceRepository.deleteById(id);
    }

    /**
     * Searches for IT resources based on the given search criteria.
     *
     * @param searchDto The search criteria.
     * @return A list of matching IT resources.
     */
    public List<ItResource> searchResources(ItResourceSearchDto searchDto) {
        Specification<ItResource> spec = Specification.where(null);

        if (searchDto.getName() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name"), "%" + searchDto.getName() + "%"));
        }
        if (searchDto.getType() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("type"), searchDto.getType()));
        }
        if (searchDto.getStatus() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("status"), searchDto.getStatus()));
        }
        if (searchDto.getSerialNumber() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("serialNumber"), "%" + searchDto.getSerialNumber() + "%"));
        }
        if (searchDto.getStartDate() != null && searchDto.getEndDate() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.between(root.get("createdAt"), searchDto.getStartDate(), searchDto.getEndDate()));
        }

        Sort sort = Sort.unsorted();
        if (searchDto.getSort() != null && searchDto.getSortDirection() != null) {
            sort = Sort.by(searchDto.getSortDirection() == SortDirection.ASCENDING ? Sort.Direction.ASC : Sort.Direction.DESC, searchDto.getSort());
        }

        return itResourceRepository.findAll(spec, sort);
    }
}