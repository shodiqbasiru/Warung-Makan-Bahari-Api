package com.enigma.wmb_api.specification;

import  com.enigma.wmb_api.dto.request.PaginationCustomerRequest;
import com.enigma.wmb_api.entity.Customer;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification {
    public static Specification<Customer> getSpecification(PaginationCustomerRequest request) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            if (request.getName() != null) {
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("customerName")),
                                "%"+request.getName().toLowerCase()+"%"
                        )
                );
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
