package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.CustomerRequest;
import com.enigma.wmb_api.dto.request.PaginationRequest;
import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.repository.CustomerRepository;
import com.enigma.wmb_api.service.CustomerService;
import com.enigma.wmb_api.specification.CustomerSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Transactional
    @Override
    public Customer create(CustomerRequest request) {
        Customer newCustomer = Customer.builder()
                .customerName(request.getCustomerName())
                .phoneNumber(request.getPhoneNumber())
                .build();
        return customerRepository.saveAndFlush(newCustomer);
    }

    @Override
    public Customer getById(String id) {
        return findBydIdThrowNotFound(id);
    }

    @Override
    public Page<Customer> getAll(PaginationRequest request) {
        if (request.getPage() < 0) request.setPage(1);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize(), sort);
        Specification<Customer> specification = CustomerSpecification.getSpecification(request);

        return customerRepository.findAll(specification, pageable);
    }

    @Transactional
    @Override
    public Customer update(Customer customer) {
        findBydIdThrowNotFound(customer.getId());
        return customerRepository.saveAndFlush(customer);
    }

    @Override
    public void delete(String id) {
        Customer currentCustomer = findBydIdThrowNotFound(id);
        customerRepository.delete(currentCustomer);
    }

    private Customer findBydIdThrowNotFound(String id) {
        return customerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Not Found"));
    }
}
