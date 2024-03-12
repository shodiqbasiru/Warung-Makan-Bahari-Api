package com.enigma.wmb_api.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginationBillRequest {
    private Integer page;
    private Integer size;
    private String sortBy;
    private String direction;
    private String startDate;
    private String endDate;
    private String customerId;
}
