package com.enigma.wmb_api.specification;

import com.enigma.wmb_api.dto.request.PaginationCustomerRequest;
import com.enigma.wmb_api.dto.request.PaginationMenuRequest;
import com.enigma.wmb_api.entity.Menu;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MenuSpecifcation {
    public static Specification<Menu> getSpecification(PaginationMenuRequest request){
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            if (request.getName()!=null){
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("menuName")),
                                "%"+request.getName().toLowerCase()+"%"
                        )
                );
            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
