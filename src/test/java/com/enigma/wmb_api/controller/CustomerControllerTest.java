package com.enigma.wmb_api.controller;

import com.enigma.wmb_api.constant.RouteApi;
import com.enigma.wmb_api.constant.UserRole;
import com.enigma.wmb_api.dto.request.PaginationCustomerRequest;
import com.enigma.wmb_api.dto.request.UpdateCustomerRequest;
import com.enigma.wmb_api.dto.response.AccountResponse;
import com.enigma.wmb_api.dto.response.CommonResponse;
import com.enigma.wmb_api.dto.response.CustomerResponse;
import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.entity.Role;
import com.enigma.wmb_api.entity.UserAccount;
import com.enigma.wmb_api.service.CustomerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {
    @MockBean
    private CustomerService customerService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testGetAll_shouldReturn201_whenGetAllCustomer() throws Exception {
        Integer page = 0;
        Integer size = 10;
        String sortBy = "customerName";
        String direction = "asc";
        String customerName = "fulan";
        PaginationCustomerRequest payload = PaginationCustomerRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .direction(direction)
                .name(customerName)
                .build();

        UserAccount account1 = UserAccount.builder()
                .id("accountId_1")
                .email("fulan@gmail.com")
                .isVerified(true)
                .roles(List.of(new Role("1", UserRole.ROLE_CUSTOMER)))
                .build();
        UserAccount account2 = UserAccount.builder()
                .id("accountId_2")
                .email("mail@gmail.com")
                .isVerified(true)
                .roles(List.of(new Role("1", UserRole.ROLE_CUSTOMER)))
                .build();
        List<Customer> mockCustomerResponse = List.of(
                new Customer("1", "fulan", "08123456789", account1),
                new Customer("2", "mail", "08123456789", account2)
        );

        Pageable pageable = PageRequest.of(payload.getPage(), payload.getSize(), Sort.by(payload.getSortBy()));
        Page<Customer> customerPage = new PageImpl<>(mockCustomerResponse, pageable, mockCustomerResponse.size());

        when(customerService.getAll(any(PaginationCustomerRequest.class))).thenReturn(customerPage);

        String stringJson = objectMapper.writeValueAsString(payload);
        mockMvc.perform(
                        MockMvcRequestBuilders.get(RouteApi.CUSTOMER_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(stringJson)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<List<CustomerResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNotNull(response);
                    assertEquals(200, response.getStatusCode());
                    assertEquals("Get all data successfully", response.getMessage());
                });
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testFindById_shouldReturn200_whenFindCustomerById() throws Exception {
        UserAccount account1 = UserAccount.builder()
                .id("accountId_1")
                .email("fulan@gmail.com")
                .isVerified(true)
                .roles(List.of(new Role("1", UserRole.ROLE_CUSTOMER)))
                .build();
        Customer customer = Customer.builder()
                .id("1")
                .customerName("fulan")
                .phoneNumber("08123456789")
                .userAccount(account1)
                .build();
        AccountResponse accountResponse = AccountResponse.builder()
                .id(account1.getId())
                .email(account1.getEmail())
                .roles(Collections.singletonList(account1.getRoles().toString()))
                .build();
        CustomerResponse.builder()
                .id(customer.getId())
                .customerName(customer.getCustomerName())
                .phoneNumber(customer.getPhoneNumber())
                .accountDetails(accountResponse)
                .build();

        when(customerService.getById(customer.getId())).thenReturn(customer);
        mockMvc.perform(
                        MockMvcRequestBuilders.get(RouteApi.CUSTOMER_PATH + "/" + customer.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<CustomerResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNotNull(response);
                    assertEquals(200, response.getStatusCode());
                    assertEquals("Get data successfully", response.getMessage());
                });

    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testUpdate_shouldReturn201_whenUpdateCustomer() throws Exception {
        UserAccount account1 = UserAccount.builder()
                .id("accountId_1")
                .email("fulan@gmail.com")
                .isVerified(true)
                .roles(List.of(new Role("1", UserRole.ROLE_CUSTOMER)))
                .build();
        Customer oldCustomer = Customer.builder()
                .id("1")
                .customerName("fulan")
                .phoneNumber("08123456789")
                .userAccount(account1)
                .build();
        UpdateCustomerRequest updateCustomerRequest = UpdateCustomerRequest.builder()
                .id(oldCustomer.getId())
                .customerName("fulanah")
                .phoneNumber("08123456789")
                .build();
        Customer updateCustomer = Customer.builder()
                .id(updateCustomerRequest.getId())
                .customerName(updateCustomerRequest.getCustomerName())
                .phoneNumber(updateCustomerRequest.getPhoneNumber())
                .userAccount(account1)
                .build();
        AccountResponse accountResponse = AccountResponse.builder()
                .id(account1.getId())
                .email(account1.getEmail())
                .roles(Collections.singletonList(account1.getRoles().toString()))
                .build();
        CustomerResponse.builder()
                .id(updateCustomer.getId())
                .customerName(updateCustomer.getCustomerName())
                .phoneNumber(updateCustomer.getPhoneNumber())
                .accountDetails(accountResponse)
                .build();

        when(customerService.update(any())).thenReturn(updateCustomer);
        objectMapper.writeValueAsString(updateCustomerRequest);
        mockMvc.perform(
                        MockMvcRequestBuilders.put(RouteApi.CUSTOMER_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateCustomerRequest))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<CustomerResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    assertNotNull(response);
                    assertEquals(201, response.getStatusCode());
                    assertEquals("Updated data successfully", response.getMessage());
                    assertNotEquals(oldCustomer.getCustomerName(), updateCustomer.getCustomerName());
                });

    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testDelete_shouldReturn200_whenDeleteCustomer() {
        String id = "1";
        doNothing().when(customerService).delete(id);
        assertDoesNotThrow(() -> mockMvc.perform(
                        MockMvcRequestBuilders.delete(RouteApi.CUSTOMER_PATH + "/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(result -> {
                    CommonResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    verify(customerService, times(1)).delete(id);
                    assertEquals(200, response.getStatusCode());
                    assertEquals("Deleted data successfully", response.getMessage());
                })
        );
    }
}