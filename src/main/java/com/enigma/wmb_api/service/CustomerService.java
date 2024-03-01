package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.CustomerRequest;
import com.enigma.wmb_api.dto.request.PaginationCustomerRequest;
import com.enigma.wmb_api.entity.Customer;
import org.springframework.data.domain.Page;

public interface CustomerService {
    Customer create(CustomerRequest request);
    Customer getById(String id);
    Page<Customer> getAll(PaginationCustomerRequest request);
    Customer update(CustomerRequest request, String id);
    void delete(String id);
}
