package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.UpdateCustomerRequest;
import com.enigma.wmb_api.dto.request.PaginationCustomerRequest;
import com.enigma.wmb_api.entity.Customer;
import org.springframework.data.domain.Page;

public interface CustomerService {
    Customer create(Customer customer);
    Customer getById(String id);
    Page<Customer> getAll(PaginationCustomerRequest request);
    Customer update(UpdateCustomerRequest request);
    void delete(String id);
}
