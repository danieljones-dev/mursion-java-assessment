package com.mursion.assignment.itresources.dto.it_resource;


import com.mursion.assignment.itresources.model.ItResource.ResourceType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;


/**
 * Data Transfer Object (DTO) for IT Resource.
 * This DTO is used for creating new IT resources.
 */
@Data
@Builder
@Schema(description = "Data Transfer Object for IT Resource")
public class ItResourceDto {

    /**
     * Name of the IT resource.
     */
    @NotBlank(message = "Name cannot be blank")
    @Schema(description = "Name of the IT resource", example = "Dell Laptop")
    private String name;

    /**
     * Type of the IT resource.
     */
    @NotNull(message = "Type cannot be null")
    @Schema(description = "Type of the IT resource", example = "LAPTOP")
    private ResourceType type;

    /**
     * Serial number of the IT resource.
     */
    @NotBlank(message = "Serial number cannot be blank")
    @Schema(description = "Serial number of the IT resource", example = "SN123456")
    private String serialNumber;

    /**
     * Inventory location of the IT resource.
     */
    @NotBlank(message = "Inventory location cannot be blank")
    @Schema(description = "Inventory location of the IT resource", example = "Room 101")
    private String inventoryLocation;
}