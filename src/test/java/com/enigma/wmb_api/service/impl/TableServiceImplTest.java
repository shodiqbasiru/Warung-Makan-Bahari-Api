package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.TableRequest;
import com.enigma.wmb_api.entity.MTable;
import com.enigma.wmb_api.repository.TableRepository;
import com.enigma.wmb_api.service.TableService;
import com.enigma.wmb_api.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TableServiceImplTest {
    @Mock
    private TableRepository tableRepository;
    @Mock
    private ValidationUtil validationUtil;
    private TableService tableService;

    @BeforeEach
    void setUp() {
        tableService = new TableServiceImpl(tableRepository, validationUtil);
    }

    @Test
    void create_createsNewTable_whenValidRequest() {
        TableRequest request = new TableRequest();
        request.setTableName("Test Table");

        MTable newTable = MTable.builder()
                .tableName(request.getTableName())
                .build();

        doNothing().when(validationUtil).validate(request);
        when(tableRepository.saveAndFlush(Mockito.any(MTable.class))).thenReturn(newTable);

        MTable createdTable = tableService.create(request);

        assertNotNull(createdTable);
        assertEquals("Test Table", createdTable.getTableName());
    }

    @Test
    void getById_returnsTable_whenTableExists() {
        String id = "1";
        MTable table = MTable.builder().tableName("Test Table").build();

        when(tableRepository.findById(id)).thenReturn(Optional.of(table));

        MTable returnedTable = tableService.getById(id);

        assertNotNull(returnedTable);
        assertEquals("Test Table", returnedTable.getTableName());
    }

    @Test
    void getById_throwsResponseStatusException_whenTableDoesNotExist() {
        String id = "1";

        when(tableRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> tableService.getById(id));
    }

    @Test
    void getAll_returnsAllTables() {
        List<MTable> tables = List.of(
                MTable.builder().tableName("Table 1").build(),
                MTable.builder().tableName("Table 2").build()
        );

        when(tableRepository.findAll()).thenReturn(tables);

        List<MTable> returnedTables = tableService.getAll();

        assertEquals(2, returnedTables.size());
    }

    @Test
    void update_updatesTable_whenValidRequest() {
        MTable request = MTable.builder().id("1").tableName("Updated Table").build();

        when(tableRepository.findById(request.getId())).thenReturn(Optional.of(request));
        when(tableRepository.saveAndFlush(Mockito.any(MTable.class))).thenReturn(request);

        MTable updatedTable = tableService.update(request);

        assertNotNull(updatedTable);
        assertEquals("Updated Table", updatedTable.getTableName());
    }

    @Test
    void update_throwsResponseStatusException_whenTableDoesNotExist() {
        MTable request = MTable.builder().id("1").tableName("Updated Table").build();

        when(tableRepository.findById(request.getId())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> tableService.update(request));
    }

    @Test
    void delete_deletesTable_whenTableExists() {
        String id = "1";
        MTable table = MTable.builder().id(id).tableName("Test Table").build();

        when(tableRepository.findById(id)).thenReturn(Optional.of(table));
        doNothing().when(tableRepository).delete(table);

        tableService.delete(id);

        verify(tableRepository, times(1)).delete(table);
    }

    @Test
    void delete_throwsResponseStatusException_whenTableDoesNotExist() {
        String id = "1";

        when(tableRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> tableService.delete(id));
    }
}