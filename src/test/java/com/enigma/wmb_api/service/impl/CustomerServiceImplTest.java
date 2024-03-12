package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.PaginationCustomerRequest;
import com.enigma.wmb_api.dto.request.UpdateCustomerRequest;
import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.repository.CustomerRepository;
import com.enigma.wmb_api.service.CustomerService;
import com.enigma.wmb_api.util.ValidationUtil;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ValidationUtil validationUtil;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerServiceImpl(customerRepository, validationUtil);
    }
    @Test
    void create_savesAndReturnsCustomer() {
        Customer customer = new Customer();
        customer.setCustomerName("Test Customer");

        when(customerRepository.saveAndFlush(any(Customer.class))).thenReturn(customer);

        Customer createdCustomer = customerService.create(customer);

        assertNotNull(createdCustomer);
        assertEquals("Test Customer", createdCustomer.getCustomerName());
    }

    @Test
    void getById_returnsCustomer_whenCustomerExists() {
        String id = "1";
        Customer customer = new Customer();
        customer.setCustomerName("Test Customer");

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        Customer returnedCustomer = customerService.getById(id);

        assertNotNull(returnedCustomer);
        assertEquals("Test Customer", returnedCustomer.getCustomerName());
    }

    @Test
    void getById_throwsResponseStatusException_whenCustomerDoesNotExist() {
        String id = "1";

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> customerService.getById(id));
    }

    @Test
    void getAll_returnsAllCustomers() {
        PaginationCustomerRequest request = new PaginationCustomerRequest();
        request.setPage(1);
        request.setSize(2);
        request.setSortBy("customerName");
        request.setDirection("ASC");

        List<Customer> customers = List.of(
                new Customer("1", "Test Customer 1", "1234567890", null),
                new Customer("2", "Test Customer 2", "1234567890", null)
        );

        Page<Customer> page = new PageImpl<>(customers);

        when(customerRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<Customer> returnedCustomers = customerService.getAll(request);

        assertEquals(2, returnedCustomers.getTotalElements());
    }

    @Test
    void update_updatesAndReturnsCustomer_whenCustomerExists() {
        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setId("1");
        request.setCustomerName("Updated Customer");
        request.setPhoneNumber("1234567890");

        Customer customer = new Customer();
        customer.setCustomerName("Test Customer");

        when(customerRepository.findById(request.getId())).thenReturn(Optional.of(customer));
        when(customerRepository.saveAndFlush(any(Customer.class))).thenReturn(customer);

        Customer updatedCustomer = customerService.update(request);

        assertNotNull(updatedCustomer);
        assertEquals("Updated Customer", updatedCustomer.getCustomerName());
    }

    @Test
    void update_throwsResponseStatusException_whenCustomerDoesNotExist() {
        UpdateCustomerRequest request = new UpdateCustomerRequest();
        request.setId("1");
        request.setCustomerName("Updated Customer");
        request.setPhoneNumber("1234567890");

        when(customerRepository.findById(request.getId())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> customerService.update(request));
    }

    @Test
    void delete_deletesCustomer_whenCustomerExists() {
        String id = "1";
        Customer customer = new Customer();
        customer.setCustomerName("Test Customer");

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).delete(customer);

        customerService.delete(id);

        verify(customerRepository, times(1)).delete(customer);
    }

    @Test
    void delete_throwsResponseStatusException_whenCustomerDoesNotExist() {
        String id = "1";

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> customerService.delete(id));
    }

}