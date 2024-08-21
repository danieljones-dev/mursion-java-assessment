package com.mursion.assignment.itresources.service;

import com.mursion.assignment.itresources.config.BaseIntegrationTest;
import com.mursion.assignment.itresources.dto.it_resource.ItResourceSearchDto;
import com.mursion.assignment.itresources.model.ItResource;
import com.mursion.assignment.itresources.repository.ItResourceRepository;
import com.mursion.assignment.itresources.types.enums.SortDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
public class ItResourceServiceTest extends BaseIntegrationTest {


    @Autowired
    private ItResourceService itResourceService;

    @Autowired
    private ItResourceRepository itResourceRepository;


    @BeforeEach
    void setUp() {
        itResourceRepository.deleteAll();
    }

    @Test
    void testSaveResource() {
        ItResource resource = createTestResource();
        ItResource savedResource = itResourceService.saveResource(resource);

        assertThat(savedResource.getId()).isNotNull();
        assertThat(savedResource.getName()).isEqualTo(resource.getName());
    }

    @Test
    void testGetResourceById() {
        ItResource resource = createAndSaveTestResource();
        Optional<ItResource> foundResource = itResourceService.getResourceById(resource.getId());

        assertThat(foundResource).isPresent();
        assertThat(foundResource.get().getId()).isEqualTo(resource.getId());
    }

    @Test
    void testGetAllResources() {
        createAndSaveTestResource();
        createAndSaveTestResource();

        List<ItResource> resources = itResourceService.getAllResources();

        assertThat(resources).hasSize(2);
    }

    @Test
    void testUpdateResource() {
        ItResource resource = createAndSaveTestResource();
        resource.setName("Updated Resource");

        ItResource updatedResource = itResourceService.updateResource(resource.getId(), resource);

        assertThat(updatedResource.getName()).isEqualTo("Updated Resource");
    }

    @Test
    void testDeleteResource() {
        ItResource resource = createAndSaveTestResource();
        itResourceService.deleteResource(resource.getId());

        Optional<ItResource> deletedResource = itResourceRepository.findById(resource.getId());
        assertThat(deletedResource).isEmpty();
    }

    @Test
    void testSearchResources() {
        ItResource resource1 = createAndSaveTestResource("Laptop 1", ItResource.ResourceType.LAPTOP);
        ItResource resource2 = createAndSaveTestResource("Monitor 1", ItResource.ResourceType.MONITOR);
        ItResource resource3 = createAndSaveTestResource("Laptop 2", ItResource.ResourceType.LAPTOP);

        ItResourceSearchDto searchDto = new ItResourceSearchDto();
        searchDto.setName("Laptop");
        searchDto.setType(ItResource.ResourceType.LAPTOP);
        searchDto.setSort("name");
        searchDto.setSortDirection(SortDirection.ASCENDING);

        List<ItResource> foundResources = itResourceService.searchResources(searchDto);

        assertThat(foundResources).hasSize(2);
        assertThat(foundResources).extracting(ItResource::getName)
                .containsExactly("Laptop 1", "Laptop 2");
    }

    private ItResource createTestResource() {
        ItResource resource = new ItResource();
        resource.setName("Test Resource");
        resource.setType(ItResource.ResourceType.LAPTOP);
        resource.setStatus(ItResource.ResourceStatus.NOT_ASSIGNED);
        resource.setSerialNumber("SN123456");
        resource.setInventoryLocation("Office A");
        return resource;
    }

    private ItResource createAndSaveTestResource() {
        return itResourceRepository.save(createTestResource());
    }

    private ItResource createAndSaveTestResource(String name, ItResource.ResourceType type) {
        ItResource resource = createTestResource();
        resource.setName(name);
        resource.setType(type);
        return itResourceRepository.save(resource);
    }
}