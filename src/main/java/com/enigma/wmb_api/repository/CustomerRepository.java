package com.enigma.wmb_api.repository;

import com.enigma.wmb_api.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}
