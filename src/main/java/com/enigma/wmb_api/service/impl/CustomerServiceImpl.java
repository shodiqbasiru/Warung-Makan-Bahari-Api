package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.UpdateCustomerRequest;
import com.enigma.wmb_api.dto.request.PaginationCustomerRequest;
import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.entity.UserAccount;
import com.enigma.wmb_api.repository.CustomerRepository;
import com.enigma.wmb_api.service.CustomerService;
import com.enigma.wmb_api.service.UserService;
import com.enigma.wmb_api.specification.CustomerSpecification;
import com.enigma.wmb_api.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ValidationUtil validation;
    private final UserService userService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Customer create(Customer customer) {
        return customerRepository.saveAndFlush(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public Customer getById(String id) {
        return findBydIdThrowNotFound(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Customer> getAll(PaginationCustomerRequest request) {
        if (request.getPage() <= 0) request.setPage(1);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize(), sort);
        Specification<Customer> specification = CustomerSpecification.getSpecification(request);

        Page<Customer> customerPage = customerRepository.findAll(specification, pageable);
        List<Customer> enabledCustomers = customerPage.getContent().stream()
                .filter(customer -> customer.getUserAccount() != null && customer.getUserAccount().isEnabled())
                .toList();

        return new PageImpl<>(enabledCustomers, pageable, enabledCustomers.size());
    }

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll().stream()
                .filter(customer -> customer.getUserAccount() != null && customer.getUserAccount().isEnabled())
                .toList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Customer update(UpdateCustomerRequest request) {
        Customer customer = findBydIdThrowNotFound(request.getId());
        validation.validate(request);

        customer.setCustomerName(request.getCustomerName());
        customer.setPhoneNumber(request.getPhoneNumber());

        return customerRepository.saveAndFlush(customer);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        Customer currentCustomer = findBydIdThrowNotFound(id);
        UserAccount userAccount = userService.getByUserId(currentCustomer.getUserAccount().getId());
        userAccount.setIsEnable(false);
        userAccount.setIsVerified(false);
    }

    private Customer findBydIdThrowNotFound(String id) {
        return customerRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer Not Found"));
    }
}
