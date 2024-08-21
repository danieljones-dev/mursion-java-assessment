package com.mursion.assignment.itresources.controller;

import com.mursion.assignment.itresources.model.ItResourceAssign;
import com.mursion.assignment.itresources.service.ItResourceAssignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing IT resource assignments.
 */
@RestController
@RequestMapping("/api/assignments")
public class ItResourceAssignController {

    private final ItResourceAssignService itResourceAssignService;

    @Autowired
    public ItResourceAssignController(ItResourceAssignService itResourceAssignService) {
        this.itResourceAssignService = itResourceAssignService;
    }

    /**
     * Creates a new assignment for an IT resource.
     *
     * @param assignment The assignment details.
     * @return The created assignment.
     */
    @Operation(summary = "Create a new assignment", description = "Creates a new assignment for an IT resource")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignment created successfully",
                    content = @Content(schema = @Schema(implementation = ItResourceAssign.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<ItResourceAssign> createAssignment(@RequestBody ItResourceAssign assignment) {
        return ResponseEntity.ok(itResourceAssignService.saveAssignment(assignment));
    }

    /**
     * Retrieves an assignment by its unique ID.
     *
     * @param id The ID of the assignment.
     * @return The assignment if found, or 404 Not Found.
     */
    @Operation(summary = "Get an assignment by ID", description = "Retrieves an assignment by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignment found",
                    content = @Content(schema = @Schema(implementation = ItResourceAssign.class))),
            @ApiResponse(responseCode = "404", description = "Assignment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ItResourceAssign> getAssignment(@PathVariable UUID id) {
        return itResourceAssignService.getAssignmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves a list of all assignments.
     *
     * @return A list of all assignments.
     */
    @GetMapping
    @Operation(summary = "Get all assignments", description = "Retrieves a list of all assignments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignments retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ItResourceAssign.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ItResourceAssign>> getAllAssignments() {
        return ResponseEntity.ok(itResourceAssignService.getAllAssignments());
    }

    /**
     * Searches for assignments based on resource ID and/or user ID.
     *
     * @param resourceId The ID of the resource (optional).
     * @param userId     The ID of the user (optional).
     * @return A list of matching assignments.
     */
    @GetMapping("/search")
    @Operation(summary = "Search for assignments", description = "Searches for assignments based on resource ID and/or user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignments retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ItResourceAssign.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<ItResourceAssign>> searchAssignments(
            @RequestParam(required = false) UUID resourceId,
            @RequestParam(required = false) UUID userId) {
        return ResponseEntity.ok(itResourceAssignService.searchAssignments(resourceId, userId));
    }

    /**
     * Updates an existing assignment.
     *
     * @param id         The ID of the assignment to update.
     * @param assignment The updated assignment details.
     * @return The updated assignment.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an assignment", description = "Updates an existing assignment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assignment updated successfully",
                    content = @Content(schema = @Schema(implementation = ItResourceAssign.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Assignment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ItResourceAssign> updateAssignment(@PathVariable UUID id, @RequestBody ItResourceAssign assignment) {
        return ResponseEntity.ok(itResourceAssignService.updateAssignment(id, assignment));
    }

    /**
     * Deletes an assignment by its unique ID.
     *
     * @param id The ID of the assignment to delete.
     * @return 204 No Content if successful, or 404 Not Found.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an assignment", description = "Deletes an assignment by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Assignment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Assignment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteAssignment(@PathVariable UUID id) {
        itResourceAssignService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Marks an assigned resource as returned.
     *
     * @param id The ID of the assignment.
     * @return The updated assignment with the returned status.
     */
    @PostMapping("/{id}/return")
    @Operation(summary = "Return an assigned resource", description = "Marks an assigned resource as returned")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resource returned successfully",
                    content = @Content(schema = @Schema(implementation = ItResourceAssign.class))),
            @ApiResponse(responseCode = "404", description = "Assignment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ItResourceAssign> returnResource(@PathVariable UUID id) {
        return ResponseEntity.ok(itResourceAssignService.returnResource(id));
    }
}