package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.entity.Bill;
import com.enigma.wmb_api.entity.BillDetail;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.repository.BillDetailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BillDetailServiceImplTest {

    @Mock
    private BillDetailRepository billDetailRepository;

    private BillDetailServiceImpl billDetailService;

    @BeforeEach
    void setUp() {
        billDetailService = new BillDetailServiceImpl(billDetailRepository);
    }

    @Test
    void createBulkShouldReturnSavedBillDetails() {
        // Given
        BillDetail billDetail1 = BillDetail.builder()
                .id("1")
                .bill(Bill.builder().id("1").build())
                .qty(1)
                .price(1000)
                .menu(Menu.builder().id("menuId").menuName("name").build())
                .build();

        BillDetail billDetail2 = BillDetail.builder()
                .id("2")
                .bill(Bill.builder().id("2").build())
                .qty(3)
                .price(4000)
                .menu(Menu.builder().id("menuId2").menuName("nameA").build())
                .build();
        List<BillDetail> billDetails = Arrays.asList(billDetail1, billDetail2);

        when(billDetailRepository.saveAllAndFlush(any())).thenReturn(billDetails);

        // When
        List<BillDetail> returnedBillDetails = billDetailService.createBulk(billDetails);

        // Then
        assertEquals(billDetails, returnedBillDetails);
        verify(billDetailRepository, times(1)).saveAllAndFlush(billDetails);
    }
}