package com.mursion.assignment.itresources.controller;


import com.mursion.assignment.itresources.dto.it_resource.ItResourceDto;
import com.mursion.assignment.itresources.dto.it_resource.ItResourceSearchDto;
import com.mursion.assignment.itresources.model.ItResource;
import com.mursion.assignment.itresources.service.ItResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing IT resources.
 */
@RestController
@RequestMapping("/api/resources")
public class ItResourceController {

    private final ItResourceService itResourceService;

    @Autowired
    public ItResourceController(ItResourceService itResourceService) {
        this.itResourceService = itResourceService;
    }

    /**
     * Creates a new IT resource.
     *
     * @param resource The IT resource details.
     * @return The created IT resource.
     */
    @PostMapping
    @Operation(summary = "Create a new IT resource", description = "Creates a new IT resource")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resource created successfully",
                    content = @Content(schema = @Schema(implementation = ItResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ItResource> createResource(@Valid @RequestBody ItResourceDto resource) {

        // create a new ItResource object from the ItResourceDto object
        ItResource itResource = new ItResource();
        itResource.setName(resource.getName());
        itResource.setType(resource.getType());
        itResource.setSerialNumber(resource.getSerialNumber());
        itResource.setInventoryLocation(resource.getInventoryLocation());
        itResource.setStatus(ItResource.ResourceStatus.NOT_ASSIGNED);

        // now, save the ItResource object using the service
        return ResponseEntity.ok(itResourceService.saveResource(itResource));
    }

    /**
     * Retrieves an IT resource by its unique ID.
     *
     * @param id The ID of the IT resource.
     * @return The IT resource if found, or 404 Not Found.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get an IT resource by ID", description = "Retrieves an IT resource by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resource found",
                    content = @Content(schema = @Schema(implementation = ItResource.class))),
            @ApiResponse(responseCode = "404", description = "Resource not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ItResource> getResource(@PathVariable UUID id) {
        return itResourceService.getResourceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves a list of all IT resources.
     *
     * @return A list of all IT resources.
     */
    @GetMapping
    @Operation(summary = "Get all IT resources", description = "Retrieves a list of all IT resources")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resources retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ItResource.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ItResource>> getAllResources() {
        return ResponseEntity.ok(itResourceService.getAllResources());
    }

    /**
     * Searches for IT resources based on name and/or type.
     *
     * @param searchDto The search criteria.
     * @return A list of matching IT resources.
     */
    @PostMapping("/search")
    @Operation(summary = "Search for IT resources", description = "Searches for IT resources based on name and/or type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resources retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ItResource.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ItResource>> searchResources(@Valid @RequestBody ItResourceSearchDto searchDto) {
        return ResponseEntity.ok(itResourceService.searchResources(searchDto));
    }

    /**
     * Updates an existing IT resource.
     *
     * @param id       The ID of the IT resource to update.
     * @param resource The updated IT resource details.
     * @return The updated IT resource.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an IT resource", description = "Updates an existing IT resource")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resource updated successfully",
                    content = @Content(schema = @Schema(implementation = ItResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Resource not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ItResource> updateResource(@PathVariable UUID id, @RequestBody ItResource resource) {
        return ResponseEntity.ok(itResourceService.updateResource(id, resource));
    }

    /**
     * Deletes an IT resource by its unique ID.
     *
     * @param id The ID of the IT resource to delete.
     * @return 204 No Content if successful, or 404 Not Found.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an IT resource", description = "Deletes an IT resource by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Resource deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Resource not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteResource(@PathVariable UUID id) {
        itResourceService.deleteResource(id);
        return ResponseEntity.noContent().build();
    }
}