package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.RouteApi;
import com.enigma.wmb_api.constant.TransTypeEnum;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.entity.TransType;
import com.enigma.wmb_api.service.TransTypeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class TransTypeControllerTest {
    @MockBean
    private TransTypeService transTypeService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testGetAll_shouldReturn200_whenGetAllTransType() throws Exception {
        List<TransType> payload = List.of(
                new TransType(TransTypeEnum.EI, "Eat In"),
                new TransType(TransTypeEnum.TA, "Take Away")
        );

        when(transTypeService.getAll()).thenReturn(payload);
        mockMvc.perform(
                        MockMvcRequestBuilders.get(RouteApi.TRANS_TYPE_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<List<TransType>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNotNull(payload);
                    assertEquals(payload.size(), 2);
                    assertEquals(200, response.getStatusCode());
                    assertEquals("Get all data successfully", response.getMessage());
                });
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testFindById_shouldReturn200_whenFindById() throws Exception {
        TransType payload = new TransType(TransTypeEnum.EI, "Eat In");

        when(transTypeService.getById(payload.getId().toString())).thenReturn(payload);
        mockMvc.perform(
                        MockMvcRequestBuilders.get(RouteApi.TRANS_TYPE_PATH + "/" + payload.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<TransType> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNotNull(payload);
                    assertEquals(200, response.getStatusCode());
                    assertEquals("Get data successfully", response.getMessage());
                });
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testUpdate_shouldReturn2001_whenUpdateTransType() throws Exception {
        TransType payload = TransType.builder()
                .id(TransTypeEnum.TA)
                .description("Take Away")
                .build();

        when(transTypeService.update(payload)).thenReturn(payload);
        String stringJson = objectMapper.writeValueAsString(payload);

        mockMvc.perform(
                        MockMvcRequestBuilders.put(RouteApi.TRANS_TYPE_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(stringJson)
        )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<TransType> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNotNull(payload);
                    assertEquals(201, response.getStatusCode());
                    assertEquals("Updated data successfully", response.getMessage());
                });
    }
}