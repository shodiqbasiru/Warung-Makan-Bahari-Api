package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.TransTypeEnum;
import com.enigma.wmb_api.dto.request.BillDetailRequest;
import com.enigma.wmb_api.dto.request.BillRequest;
import com.enigma.wmb_api.dto.request.PaginationBillRequest;
import com.enigma.wmb_api.dto.request.UpdateTransactionStatusRequest;
import com.enigma.wmb_api.dto.response.BillResponse;
import com.enigma.wmb_api.entity.*;
import com.enigma.wmb_api.repository.BillRepository;
import com.enigma.wmb_api.service.*;
import com.enigma.wmb_api.util.ValidationUtil;
import org.hibernate.sql.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static java.awt.SystemColor.menu;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BillServiceImplTest {
    @Mock
    private BillRepository billRepository;
    @Mock
    private BillDetailService billDetailService;
    @Mock
    private CustomerService customerService;
    @Mock
    private TableService tableService;
    @Mock
    private TransTypeService transTypeService;
    @Mock
    private MenuService menuService;
    @Mock
    private ValidationUtil validationUtil;
    @Mock
    private PaymentService paymentService;
    private BillService billService;

    @BeforeEach
    void setUp() {
        billService = new BillServiceImpl(
                billRepository,
                billDetailService,
                customerService,
                tableService,
                transTypeService,
                menuService,
                validationUtil,
                paymentService
        );
    }

    @Test
    void create_createsAndReturnsBill_whenValidRequest() {
        Customer customer = Customer.builder()
                .id("customerId 1")
                .customerName("customerName")
                .build();

        TransType transType = TransType.builder()
                .id(TransTypeEnum.EI)
                .description("Eat In")
                .build();

        MTable table = MTable.builder()
                .id("tableId 1")
                .tableName("table1")
                .build();

        Menu menuA = Menu.builder()
                .id("menuId 1")
                .menuName("Menu 1")
                .price(10000)
                .build();

        Menu menuB = Menu.builder()
                .id("menuId 2")
                .menuName("Menu 2")
                .price(5000)
                .build();

        Bill bill = Bill.builder()
                .id("billId 1")
                .transType(transType)
                .customer(customer)
                .mTable(table)
                .billDetails(List.of())
                .build();

        BillDetailRequest detailRequestA = BillDetailRequest.builder()
                .menuId(menuA.getId())
                .qty(2)
                .build();

        BillDetailRequest detailRequestB = BillDetailRequest.builder()
                .menuId(menuB.getId())
                .qty(4)
                .build();

        List<BillDetailRequest> billDetailRequests = List.of(detailRequestA, detailRequestB);
        BillDetail billDetailA = BillDetail.builder()
                .id("billDetailId 1")
                .bill(bill)
                .menu(menuA)
                .qty(detailRequestA.getQty())
                .price(menuA.getPrice())
                .build();

        BillDetail billDetailB = BillDetail.builder()
                .id("billDetailId 2")
                .bill(bill)
                .menu(menuB)
                .qty(detailRequestB.getQty())
                .price(menuB.getPrice())
                .build();

        List<BillDetail> billDetails = List.of(billDetailA, billDetailB);
        bill.setBillDetails(billDetails);

        Payment payment = Payment.builder()
                .id("paymentId 1")
                .token("token")
                .redirectUrl("redirectUrl")
                .transactionStatus("transactionStatus")
                .build();
        bill.setPayment(payment);

        BillRequest request = BillRequest.builder()
                .customerId(customer.getId())
                .tableId(table.getId())
                .billDetailRequests(billDetailRequests)
                .build();

        when(customerService.getById(request.getCustomerId())).thenReturn(customer);
        when(tableService.getById(request.getTableId())).thenReturn(table);
        when(transTypeService.getAll()).thenReturn(List.of(transType));
        when(menuService.getById(detailRequestA.getMenuId())).thenReturn(menuA);
        when(menuService.getById(detailRequestB.getMenuId())).thenReturn(menuB);
        when(billRepository.saveAndFlush(any(Bill.class))).thenReturn(bill);
        when(billDetailService.createBulk(anyList())).thenReturn(billDetails);
        when(paymentService.createPayment(any())).thenReturn(payment);

        BillResponse response = billService.create(request);

        assertEquals(bill.getCustomer().getId(), response.getCustomerId());
        assertEquals(bill.getMTable().getId(), response.getTableId());
        assertEquals(bill.getTransType().getId().toString(), response.getTransType());
        assertEquals(bill.getBillDetails().size(), response.getBillDetailResponses().size());
    }

    @Test
    void getAll_returnsPagedBills_whenValidRequest() {
        PaginationBillRequest request = new PaginationBillRequest();
        request.setPage(1);
        request.setSize(10);
        request.setDirection("ASC");
        request.setSortBy("date");

        BillDetail billDetail = BillDetail.builder()
                .id("billDetailId 1")
                .menu(Menu.builder().id("menuId 1").build())
                .qty(2)
                .price(10000)
                .build();

        Payment payment = Payment.builder()
                .id("paymentId 1")
                .token("token")
                .redirectUrl("redirectUrl")
                .transactionStatus("transactionStatus")
                .build();

        Bill bill = Bill.builder()
                .id("billId 1")
                .date(new Date())
                .customer(Customer.builder().id("customerId 1").build())
                .mTable(MTable.builder().id("tableId 1").build())
                .transType(TransType.builder().id(TransTypeEnum.EI).build())
                .billDetails(List.of(billDetail))
                .payment(payment)
                .build();

        Page<Bill> mockPage = new PageImpl<>(Collections.singletonList(bill));
        when(billRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(mockPage);

        Page<BillResponse> result = billService.getAll(request);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }



    @Test
    void testUpdateStatus() {
        // Membuat objek bill dan request

        Payment payment = Payment.builder()
                .id("paymentId 1")
                .transactionStatus("NOT PAID")
                .build();

        Bill bill = Bill.builder()
                .id("billId 1")
                .date(new Date())
                .customer(Customer.builder().id("customerId 1").build())
                .mTable(MTable.builder().id("tableId 1").build())
                .transType(TransType.builder().id(TransTypeEnum.EI).build())
                .billDetails(List.of())
                .payment(payment)
                .build();


        UpdateTransactionStatusRequest request = new UpdateTransactionStatusRequest();
        request.setOrderId(bill.getId());
        request.setTransactionStatus("PAID");

        payment.setTransactionStatus(request.getTransactionStatus());

        when(billRepository.findById(bill.getId())).thenReturn(Optional.of(bill));
        billService.updateStatus(request);

        assertEquals("PAID", bill.getPayment().getTransactionStatus());
    }

}