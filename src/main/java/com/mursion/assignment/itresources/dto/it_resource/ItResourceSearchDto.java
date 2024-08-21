package com.mursion.assignment.itresources.dto.it_resource;

import com.mursion.assignment.itresources.model.ItResource;
import com.mursion.assignment.itresources.types.enums.SortDirection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) for searching IT resources.
 * This DTO allows for flexible searching based on various criteria.
 */
@Data
public class ItResourceSearchDto {

    /**
     * Name of the IT resource.
     */
    @Schema(description = "Name of the IT resource", example = "Dell Laptop")
    private String name;

    /**
     * Type of the IT resource.
     */
    @Schema(description = "Type of the IT resource", example = "LAPTOP")
    private ItResource.ResourceType type;

    /**
     * Status of the IT resource.
     */
    @Schema(description = "Status of the IT resource", example = "ASSIGNED")
    private ItResource.ResourceStatus status;

    /**
     * Start date for the search.
     */
    @Schema(description = "Start date for the search", example = "2022-01-01T00:00:00")
    private LocalDateTime startDate;

    /**
     * End date for the search.
     */
    @Schema(description = "End date for the search", example = "2022-12-31T23:59:59")
    private LocalDateTime endDate;

    /**
     * Serial number of the IT resource.
     */
    @Schema(description = "Serial number of the IT resource", example = "SN123456")
    private String serialNumber;

    /**
     * Sort field for the search results.
     */
    @Schema(description = "Sort field for the search", example = "name")
    private String sort;

    /**
     * Sort direction for the search results.
     */
    @Schema(description = "Sort direction for the search", example = "ASCENDING")
    private SortDirection sortDirection;
}