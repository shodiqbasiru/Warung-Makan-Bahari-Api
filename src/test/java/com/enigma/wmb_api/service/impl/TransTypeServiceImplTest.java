package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.TransTypeEnum;
import com.enigma.wmb_api.entity.TransType;
import com.enigma.wmb_api.repository.TransTypeRepository;
import com.enigma.wmb_api.service.TransTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TransTypeServiceImplTest {

    @Mock
    private TransTypeRepository transTypeRepository;
    private TransTypeService transTypeService;

    @BeforeEach
    void setUp() {
        transTypeService = new TransTypeServiceImpl(transTypeRepository);
    }

    @Test
    void getAll_returnsAllTransTypes() {
        List<TransType> transTypes = List.of(
                new TransType(TransTypeEnum.valueOf("EI"),"Eat In"),
                new TransType(TransTypeEnum.valueOf("TA"),"Take Away")
        );

        when(transTypeRepository.findAll()).thenReturn(transTypes);

        List<TransType> returnedTransTypes = transTypeService.getAll();

        assertEquals(2, returnedTransTypes.size());
    }

    @Test
    void getById_returnsTransType_whenTransTypeExists() {
        String id = "EI";
        TransType transType = new TransType(TransTypeEnum.valueOf(id), "Eat In");

        when(transTypeRepository.findById(TransTypeEnum.valueOf(id))).thenReturn(Optional.of(transType));

        TransType returnedTransType = transTypeService.getById(id);

        assertNotNull(returnedTransType);
        assertEquals(TransTypeEnum.valueOf(id), returnedTransType.getId());
    }

    @Test
    void getById_throwsResponseStatusException_whenTransTypeDoesNotExist() {
        String id = "EI";

        when(transTypeRepository.findById(TransTypeEnum.valueOf(id))).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> transTypeService.getById(id));
    }

    @Test
    void update_updatesTransType_whenValidRequest() {
        TransType transType = new TransType(TransTypeEnum.valueOf("EI"),"Eat In");

        when(transTypeRepository.findById(TransTypeEnum.valueOf(transType.getId().toString()))).thenReturn(Optional.of(transType));
        when(transTypeRepository.save(any(TransType.class))).thenReturn(transType);

        TransType updatedTransType = transTypeService.update(transType);

        assertNotNull(updatedTransType);
        assertEquals(transType.getId(), updatedTransType.getId());
    }

    @Test
    void update_throwsResponseStatusException_whenTransTypeDoesNotExist() {
        TransType transType = new TransType(TransTypeEnum.valueOf("EI"),"Eat In");

        when(transTypeRepository.findById(TransTypeEnum.valueOf(transType.getId().toString()))).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> transTypeService.update(transType));
    }
}