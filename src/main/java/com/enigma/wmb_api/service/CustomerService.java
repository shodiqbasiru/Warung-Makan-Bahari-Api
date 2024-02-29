package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.CustomerRequest;
import com.enigma.wmb_api.dto.request.PaginationCustomerRequest;
import com.enigma.wmb_api.entity.Customer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {
    Customer create(CustomerRequest request);
    Customer getById(String id);
    Page<Customer> getAll(PaginationCustomerRequest request);
    Customer update(Customer request);
    void delete(String id);
    Page<Customer> getAllWithPagination(Integer pageNumber, Integer pageSize, String sort);
}
