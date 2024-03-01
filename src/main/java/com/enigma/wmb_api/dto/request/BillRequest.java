package com.enigma.wmb_api.dto.request;

import jakarta.validation.constraints.NotNull;
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
    private List<BillDetailRequest> billDetailRequests;
}
