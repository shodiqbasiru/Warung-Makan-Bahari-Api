package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.RouteApi;
import com.enigma.wmb_api.dto.request.TableRequest;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.entity.MTable;
import com.enigma.wmb_api.service.TableService;
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
class TableControllerTest {
    @MockBean
    private TableService tableService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testCreate_shouldReturn201_whenCreateTable() throws Exception {
        TableRequest req = TableRequest.builder()
                .tableName("Table 1")
                .build();
        MTable mockResponse = MTable.builder()
                .id("1")
                .tableName(req.getTableName())
                .build();
        when(tableService.create(any())).thenReturn(mockResponse);
        String stringJson = objectMapper.writeValueAsString(req);
        mockMvc.perform(
                        MockMvcRequestBuilders.post(RouteApi.TABLE_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(stringJson)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<MTable> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNotNull(mockResponse);
                    assertEquals(mockResponse.getId(), response.getData().getId());
                    assertEquals(mockResponse.getTableName(), response.getData().getTableName());
                    assertEquals(201, response.getStatusCode());
                    assertEquals("Created a new table successfully", response.getMessage());
                });
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testGetAll_shouldReturn200_whenGetAllTable() throws Exception {
        List<MTable> tableList = List.of(
                MTable.builder().id("1").tableName("Table 1").build(),
                MTable.builder().id("2").tableName("Table 2").build()
        );
        when(tableService.getAll()).thenReturn(tableList);
        mockMvc.perform(
                        MockMvcRequestBuilders.get(RouteApi.TABLE_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<List<MTable>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNotNull(tableList);
                    assertEquals(200, response.getStatusCode());
                    assertEquals("Get all data successfully", response.getMessage());
                });
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testFindById_shouldReturn200_whenFindTableById() throws Exception {
        MTable table = MTable.builder()
                .id("1")
                .tableName("Table 1")
                .build();
        when(tableService.getById(table.getId())).thenReturn(table);
        mockMvc.perform(
                        MockMvcRequestBuilders.get(RouteApi.TABLE_PATH + "/" + table.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<MTable> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNotNull(table);
                    assertEquals(200, response.getStatusCode());
                    assertEquals("Get data successfully", response.getMessage());
                });

    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testUpdate_shouldReturn201_whenUpdateTable() throws Exception {
        MTable oldTable = MTable.builder()
                .id("1")
                .tableName("Table 1")
                .build();
        MTable newTable = MTable.builder()
                .id(oldTable.getId())
                .tableName("Table 2")
                .build();
        when(tableService.update(any())).thenReturn(newTable);
        objectMapper.writeValueAsString(newTable);
        mockMvc.perform(
                        MockMvcRequestBuilders.put(RouteApi.TABLE_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newTable))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<MTable> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNotNull(newTable);
                    assertEquals(201, response.getStatusCode());
                    assertEquals("Updated data successfully", response.getMessage());
                });
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testDelete_shouldReturn200_whenDeleteTable() {
        MTable table = MTable.builder()
                .id("1")
                .tableName("Table 1")
                .build();
        doNothing().when(tableService).delete(table.getId());
        assertDoesNotThrow(() -> {
            mockMvc.perform(
                            MockMvcRequestBuilders.delete(RouteApi.TABLE_PATH + "/" + table.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andDo(MockMvcResultHandlers.print())
                    .andDo(result -> {
                        CommonResponse<MTable> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                        });
                        verify(tableService, times(1)).delete(table.getId());
                        assertEquals(200, response.getStatusCode());
                        assertEquals("Deleted data successfully", response.getMessage());
                    });
        });
    }
}