package com.enigma.wmb_api.specification;

import com.enigma.wmb_api.dto.request.PaginationBillRequest;
import com.enigma.wmb_api.entity.Bill;
import com.enigma.wmb_api.util.DateUtil;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BillSpecification {
    public static Specification<Bill> getSpecification(PaginationBillRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (request.getStartDate()!=null){
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("date"),
                                DateUtil.parseDate(request.getStartDate(), "yyyy-MM-dd")
                        )
                );
            }

            if (request.getEndDate()!=null){
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("date"),
                                DateUtil.parseDate(request.getEndDate(), "yyyy-MM-dd")
                        )
                );
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
