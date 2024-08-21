package com.mursion.assignment.itresources.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mursion.assignment.itresources.config.BaseIntegrationTest;
import com.mursion.assignment.itresources.dto.it_resource.ItResourceDto;
import com.mursion.assignment.itresources.dto.it_resource.ItResourceSearchDto;
import com.mursion.assignment.itresources.model.ItResource;
import com.mursion.assignment.itresources.repository.ItResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class ItResourceControllerTest extends BaseIntegrationTest {



    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ItResourceRepository itResourceRepository;

    private ItResource testResource;
    private ItResourceDto testResourceDto;


    @BeforeEach
    void setUp() {
        itResourceRepository.deleteAll();

        testResourceDto = ItResourceDto.builder()
                .name("Test Resource")
                .type(ItResource.ResourceType.LAPTOP)
                .serialNumber("SN123456")
                .inventoryLocation("Office A")
                .build();

        testResource = new ItResource();
        testResource.setName(testResourceDto.getName());
        testResource.setType(testResourceDto.getType());
        testResource.setSerialNumber(testResourceDto.getSerialNumber());
        testResource.setInventoryLocation(testResourceDto.getInventoryLocation());
        testResource.setStatus(ItResource.ResourceStatus.NOT_ASSIGNED);
        testResource = itResourceRepository.save(testResource);
    }

    @Test
    void testCreateResource() throws Exception {
        mockMvc.perform(post("/api/resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testResourceDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(testResourceDto.getName()))
                .andExpect(jsonPath("$.type").value(testResourceDto.getType().toString()))
                .andExpect(jsonPath("$.serialNumber").value(testResourceDto.getSerialNumber()))
                .andExpect(jsonPath("$.inventoryLocation").value(testResourceDto.getInventoryLocation()))
                .andExpect(jsonPath("$.status").value("NOT_ASSIGNED"));
    }

    @Test
    void testGetResource() throws Exception {
        mockMvc.perform(get("/api/resources/{id}", testResource.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testResource.getId().toString()))
                .andExpect(jsonPath("$.name").value(testResource.getName()));
    }

    @Test
    void testGetAllResources() throws Exception {
        mockMvc.perform(get("/api/resources"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testResource.getId().toString()))
                .andExpect(jsonPath("$[0].name").value(testResource.getName()));
    }

    @Test
    void testSearchResources() throws Exception {
        ItResourceSearchDto searchDto = new ItResourceSearchDto();
        searchDto.setName("Test");
        searchDto.setType(ItResource.ResourceType.LAPTOP);

        mockMvc.perform(post("/api/resources/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testResource.getId().toString()))
                .andExpect(jsonPath("$[0].name").value(testResource.getName()));
    }

    @Test
    void testUpdateResource() throws Exception {
        testResource.setName("Updated Resource");

        mockMvc.perform(put("/api/resources/{id}", testResource.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testResource)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testResource.getId().toString()))
                .andExpect(jsonPath("$.name").value("Updated Resource"));
    }

    @Test
    void testDeleteResource() throws Exception {
        mockMvc.perform(delete("/api/resources/{id}", testResource.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/resources/{id}", testResource.getId()))
                .andExpect(status().isNotFound());
    }
}