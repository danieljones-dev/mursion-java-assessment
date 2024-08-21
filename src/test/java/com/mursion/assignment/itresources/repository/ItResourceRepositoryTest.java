package com.mursion.assignment.itresources.repository;

import com.mursion.assignment.itresources.model.ItResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ItResourceRepositoryTest {

    @Autowired
    private ItResourceRepository itResourceRepository;

    @BeforeEach
    void setUp() {
        itResourceRepository.deleteAll();

        createResource("Dell Laptop", ItResource.ResourceType.LAPTOP, "SN001", "Office A");
        createResource("HP Monitor", ItResource.ResourceType.MONITOR, "SN002", "Office B");
        createResource("Logitech Mouse", ItResource.ResourceType.MOUSE, "SN003", "Office A");
        createResource("Apple MacBook", ItResource.ResourceType.LAPTOP, "SN004", "Office C");
    }

    @Test
    void testFindByNameContainingIgnoreCase() {
        List<ItResource> resources = itResourceRepository.findByNameContainingIgnoreCase("laptop");
        assertThat(resources).hasSize(2);
        assertThat(resources).extracting(ItResource::getName)
                .containsExactlyInAnyOrder("Dell Laptop", "Apple MacBook");
    }

    @Test
    void testFindByType() {
        List<ItResource> resources = itResourceRepository.findByType(ItResource.ResourceType.LAPTOP.toString());
        assertThat(resources).hasSize(2);
        assertThat(resources).extracting(ItResource::getType)
                .containsOnly(ItResource.ResourceType.LAPTOP);
    }

    @Test
    void testFindByNameContainingIgnoreCaseAndType() {
        List<ItResource> resources = itResourceRepository.findByNameContainingIgnoreCaseAndType("dell", ItResource.ResourceType.LAPTOP.toString());
        assertThat(resources).hasSize(1);
        assertThat(resources.getFirst().getName()).isEqualTo("Dell Laptop");
        assertThat(resources.getFirst().getType()).isEqualTo(ItResource.ResourceType.LAPTOP);
    }

    @Test
    void testFindAllWithSpecificationAndSort() {
        Specification<ItResource> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("inventoryLocation"), "Office A");
        Sort sort = Sort.by(Sort.Direction.ASC, "name");

        List<ItResource> resources = itResourceRepository.findAll(spec, sort);
        assertThat(resources).hasSize(2);
        assertThat(resources).extracting(ItResource::getName)
                .containsExactly("Dell Laptop", "Logitech Mouse");
    }

    private void createResource(String name, ItResource.ResourceType type, String serialNumber, String inventoryLocation) {
        ItResource resource = new ItResource();
        resource.setName(name);
        resource.setType(type);
        resource.setSerialNumber(serialNumber);
        resource.setInventoryLocation(inventoryLocation);
        resource.setStatus(ItResource.ResourceStatus.NOT_ASSIGNED);
        itResourceRepository.save(resource);
    }
}