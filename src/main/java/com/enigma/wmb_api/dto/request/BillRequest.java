package com.enigma.wmb_api.dto.request;

import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillRequest {
    private String customerId;
    private String tableId;
    private String transTypeId;
    private List<BillDetailRequest> billDetailRequests;
}
