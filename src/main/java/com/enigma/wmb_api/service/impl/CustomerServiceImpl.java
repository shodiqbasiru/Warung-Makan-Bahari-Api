package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.CustomerRequest;
import com.enigma.wmb_api.dto.request.SearchCustomerRequest;
import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.repository.CustomerRepository;
import com.enigma.wmb_api.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public List<Customer> getAll() {
        return customerRepository.findAll();
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

    @Override
    public Page<Customer> getAllWithPagination(Integer pageNumber, Integer pageSize, String sort) {
        Pageable pageable;
        if (sort!=null){
            pageable = PageRequest.of(pageNumber,pageSize, Sort.Direction.ASC,sort);
        } else {
            pageable = PageRequest.of(pageNumber,pageSize);
        }
        return customerRepository.findAll(pageable);
    }

    private Customer findBydIdThrowNotFound(String id) {
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer Not Found"));
    }
}
