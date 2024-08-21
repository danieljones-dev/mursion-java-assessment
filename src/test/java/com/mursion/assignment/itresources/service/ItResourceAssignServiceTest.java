package com.mursion.assignment.itresources.service;

import com.mursion.assignment.itresources.config.BaseIntegrationTest;
import com.mursion.assignment.itresources.model.ItResource;
import com.mursion.assignment.itresources.model.ItResourceAssign;
import com.mursion.assignment.itresources.repository.ItResourceAssignRepository;
import com.mursion.assignment.itresources.repository.ItResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
public class ItResourceAssignServiceTest extends BaseIntegrationTest {

    @Autowired
    private ItResourceAssignService itResourceAssignService;

    @Autowired
    private ItResourceRepository itResourceRepository;

    @Autowired
    private ItResourceAssignRepository itResourceAssignRepository;

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
        testResource.setStatus(ItResource.ResourceStatus.NOT_ASSIGNED);
        testResource = itResourceRepository.save(testResource);

        testUserId = UUID.randomUUID();
    }

    @Test
    void testSaveAssignment() {
        ItResourceAssign assignment = new ItResourceAssign();
        assignment.setResource(testResource);
        assignment.setUserId(testUserId);
        assignment.setAssignedDate(LocalDate.now());

        ItResourceAssign savedAssignment = itResourceAssignService.saveAssignment(assignment);

        assertThat(savedAssignment.getId()).isNotNull();
        assertThat(savedAssignment.getStatus()).isEqualTo(ItResourceAssign.AssignStatus.NOT_RETURNED);
        assertThat(savedAssignment.getResource().getStatus()).isEqualTo(ItResource.ResourceStatus.ASSIGNED);
    }

    @Test
    void testSaveAssignmentWithAlreadyAssignedResource() {
        testResource.setStatus(ItResource.ResourceStatus.ASSIGNED);
        itResourceRepository.save(testResource);

        ItResourceAssign assignment = new ItResourceAssign();
        assignment.setResource(testResource);
        assignment.setUserId(testUserId);
        assignment.setAssignedDate(LocalDate.now());

        assertThrows(RuntimeException.class, () -> itResourceAssignService.saveAssignment(assignment));
    }

    @Test
    void testGetAllAssignments() {
        ItResourceAssign assignment1 = createAndSaveAssignment(testResource, UUID.randomUUID());
        ItResourceAssign assignment2 = createAndSaveAssignment(testResource, UUID.randomUUID());

        List<ItResourceAssign> assignments = itResourceAssignService.getAllAssignments();

        assertThat(assignments).hasSize(2);
        assertThat(assignments).extracting(ItResourceAssign::getId)
                .containsExactlyInAnyOrder(assignment1.getId(), assignment2.getId());
    }

    @Test
    void testSearchAssignments() {
        ItResourceAssign assignment1 = createAndSaveAssignment(testResource, testUserId);
        ItResourceAssign assignment2 = createAndSaveAssignment(testResource, UUID.randomUUID());

        List<ItResourceAssign> foundAssignments = itResourceAssignService.searchAssignments(testResource.getId(), testUserId);

        assertThat(foundAssignments).hasSize(1);
        assertThat(foundAssignments.get(0).getId()).isEqualTo(assignment1.getId());
    }

    @Test
    void testUpdateAssignment() {
        ItResourceAssign assignment = createAndSaveAssignment(testResource, testUserId);
        UUID newUserId = UUID.randomUUID();

        assignment.setUserId(newUserId);
        assignment.setStatus(ItResourceAssign.AssignStatus.RETURNED);

        ItResourceAssign updatedAssignment = itResourceAssignService.updateAssignment(assignment.getId(), assignment);

        assertThat(updatedAssignment.getUserId()).isEqualTo(newUserId);
        assertThat(updatedAssignment.getStatus()).isEqualTo(ItResourceAssign.AssignStatus.RETURNED);
    }

    @Test
    void testReturnResource() {
        ItResourceAssign assignment = createAndSaveAssignment(testResource, testUserId);

        ItResourceAssign returnedAssignment = itResourceAssignService.returnResource(assignment.getId());

        assertThat(returnedAssignment.getStatus()).isEqualTo(ItResourceAssign.AssignStatus.RETURNED);
        assertThat(returnedAssignment.getReturnDate()).isNotNull();
        assertThat(returnedAssignment.getResource().getStatus()).isEqualTo(ItResource.ResourceStatus.NOT_ASSIGNED);
    }

    private ItResourceAssign createAndSaveAssignment(ItResource resource, UUID userId) {
        ItResourceAssign assignment = new ItResourceAssign();
        assignment.setResource(resource);
        assignment.setUserId(userId);
        assignment.setStatus(ItResourceAssign.AssignStatus.NOT_RETURNED);
        assignment.setAssignedDate(LocalDate.now());
        return itResourceAssignService.saveAssignment(assignment);
    }
}