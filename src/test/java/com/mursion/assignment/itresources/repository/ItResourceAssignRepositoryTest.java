package com.mursion.assignment.itresources.repository;

import com.mursion.assignment.itresources.config.BaseIntegrationTest;
import com.mursion.assignment.itresources.model.ItResource;
import com.mursion.assignment.itresources.model.ItResourceAssign;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ItResourceAssignRepositoryTest extends BaseIntegrationTest {


    @Autowired
    private ItResourceAssignRepository itResourceAssignRepository;

    @Autowired
    private ItResourceRepository itResourceRepository;

    private ItResource testResource;
    private UUID testUserId;



    @BeforeEach
    void setUp() {
        itResourceAssignRepository.deleteAll();
        itResourceRepository.deleteAll();

        testResource = new ItResource();
        testResource.setName("Test Resource");
        testResource.setType(ItResource.ResourceType.LAPTOP);
        testResource.setSerialNumber("SN123456");
        testResource.setInventoryLocation("Office A");
        testResource.setStatus(ItResource.ResourceStatus.ASSIGNED);
        testResource = itResourceRepository.save(testResource);

        testUserId = UUID.randomUUID();
    }

    @Test
    void testFindByResourceId() {
        ItResourceAssign assignment = createAssignment(testResource, testUserId);
        itResourceAssignRepository.save(assignment);

        List<ItResourceAssign> foundAssignments = itResourceAssignRepository.findByResourceId(testResource.getId());
        assertThat(foundAssignments).hasSize(1);
        assertThat(foundAssignments.getFirst().getResource().getId()).isEqualTo(testResource.getId());
    }

    @Test
    void testFindByUserId() {
        ItResourceAssign assignment = createAssignment(testResource, testUserId);
        itResourceAssignRepository.save(assignment);

        List<ItResourceAssign> foundAssignments = itResourceAssignRepository.findByUserId(testUserId);
        assertThat(foundAssignments).hasSize(1);
        assertThat(foundAssignments.getFirst().getUserId()).isEqualTo(testUserId);
    }

    @Test
    void testFindByResourceIdAndUserId() {
        ItResourceAssign assignment = createAssignment(testResource, testUserId);
        itResourceAssignRepository.save(assignment);

        List<ItResourceAssign> foundAssignments = itResourceAssignRepository.findByResourceIdAndUserId(testResource.getId(), testUserId);
        assertThat(foundAssignments).hasSize(1);
        assertThat(foundAssignments.getFirst().getResource().getId()).isEqualTo(testResource.getId());
        assertThat(foundAssignments.getFirst().getUserId()).isEqualTo(testUserId);
    }

    private ItResourceAssign createAssignment(ItResource resource, UUID userId) {
        ItResourceAssign assignment = new ItResourceAssign();
        assignment.setResource(resource);
        assignment.setUserId(userId);
        assignment.setStatus(ItResourceAssign.AssignStatus.NOT_RETURNED);
        assignment.setAssignedDate(LocalDate.now());
        return assignment;
    }
}