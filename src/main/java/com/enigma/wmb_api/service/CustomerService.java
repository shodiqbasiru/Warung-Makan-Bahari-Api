package com.enigma.wmb_api.service;

import com.enigma.wmb_api.dto.request.CustomerRequest;
import com.enigma.wmb_api.dto.request.SearchCustomerRequest;
import com.enigma.wmb_api.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer create(CustomerRequest request);
    Customer getById(String id);
    List<Customer> getAll();
    Customer update(Customer request);
    void delete(String id);
}
