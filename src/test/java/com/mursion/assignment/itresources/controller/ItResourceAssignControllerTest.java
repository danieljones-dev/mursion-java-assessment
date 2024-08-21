package com.mursion.assignment.itresources.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mursion.assignment.itresources.config.BaseIntegrationTest;
import com.mursion.assignment.itresources.model.ItResource;
import com.mursion.assignment.itresources.model.ItResourceAssign;
import com.mursion.assignment.itresources.repository.ItResourceAssignRepository;
import com.mursion.assignment.itresources.repository.ItResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers

public class ItResourceAssignControllerTest extends BaseIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ItResourceAssignRepository assignRepository;

    @Autowired
    private ItResourceRepository resourceRepository;


    @BeforeEach
    void setUp() {
        assignRepository.deleteAll();
        resourceRepository.deleteAll();
    }

    @Test
    void testCreateAssignment() throws Exception {
        ItResource resource = new ItResource();
        // Set necessary fields for ItResource
        resource = resourceRepository.save(resource);

        ItResourceAssign assignment = new ItResourceAssign();
        assignment.setResource(resource);
        assignment.setUserId(UUID.randomUUID());
        assignment.setStatus(ItResourceAssign.AssignStatus.NOT_RETURNED);
        assignment.setAssignedDate(LocalDate.now());

        mockMvc.perform(post("/api/assignments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(assignment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.resource.id").value(resource.getId().toString()))
                .andExpect(jsonPath("$.userId").value(assignment.getUserId().toString()))
                .andExpect(jsonPath("$.status").value(assignment.getStatus().toString()));
    }

    @Test
    void testGetAssignment() throws Exception {
        ItResource resource = new ItResource();
        // Set necessary fields for ItResource
        resource = resourceRepository.save(resource);

        ItResourceAssign assignment = new ItResourceAssign();
        assignment.setResource(resource);
        assignment.setUserId(UUID.randomUUID());
        assignment.setStatus(ItResourceAssign.AssignStatus.NOT_RETURNED);
        assignment.setAssignedDate(LocalDate.now());
        ItResourceAssign savedAssignment = assignRepository.save(assignment);

        mockMvc.perform(get("/api/assignments/{id}", savedAssignment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedAssignment.getId().toString()))
                .andExpect(jsonPath("$.resource.id").value(resource.getId().toString()))
                .andExpect(jsonPath("$.userId").value(savedAssignment.getUserId().toString()))
                .andExpect(jsonPath("$.status").value(savedAssignment.getStatus().toString()));
    }

    @Test
    void testGetAllAssignments() throws Exception {
        ItResource resource1 = new ItResource();
        // Set necessary fields for ItResource
        resource1 = resourceRepository.save(resource1);

        ItResource resource2 = new ItResource();
        // Set necessary fields for ItResource
        resource2 = resourceRepository.save(resource2);

        ItResourceAssign assignment1 = new ItResourceAssign();
        assignment1.setResource(resource1);
        assignment1.setUserId(UUID.randomUUID());
        assignment1.setStatus(ItResourceAssign.AssignStatus.NOT_RETURNED);
        assignment1.setAssignedDate(LocalDate.now());
        assignRepository.save(assignment1);

        ItResourceAssign assignment2 = new ItResourceAssign();
        assignment2.setResource(resource2);
        assignment2.setUserId(UUID.randomUUID());
        assignment2.setStatus(ItResourceAssign.AssignStatus.NOT_RETURNED);
        assignment2.setAssignedDate(LocalDate.now());
        assignRepository.save(assignment2);

        mockMvc.perform(get("/api/assignments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }
}