package com.enigma.wmb_api.specification;

import com.enigma.wmb_api.dto.request.PaginationMenuRequest;
import com.enigma.wmb_api.entity.Menu;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MenuSpecification {
    public static Specification<Menu> getSpecification(PaginationMenuRequest request){
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            if (request.getMenuName()!=null){
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("menuName")),
                                "%"+request.getMenuName().toLowerCase()+"%"
                        )
                );
            }

            if (request.getMinPrice()!=null){
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("price"),
                                request.getMinPrice()
                        )
                );
            }

            if (request.getMaxPrice()!=null){
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("price"),
                                request.getMaxPrice()
                        )
                );
            }
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
