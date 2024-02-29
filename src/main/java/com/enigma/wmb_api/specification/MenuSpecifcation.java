package com.enigma.wmb_api.specification;

import com.enigma.wmb_api.dto.request.PaginationRequest;
import com.enigma.wmb_api.entity.Menu;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MenuSpecifcation {
    public static Specification<Menu> getSpecifcation(PaginationRequest request){
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
