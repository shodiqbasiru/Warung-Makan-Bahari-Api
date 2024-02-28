package com.enigma.wmb_api.dto.response;

import com.enigma.wmb_api.dto.request.BillDetailRequest;
import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillResponse {
    private String id;
    private Date date;
    private String customerId;
    private String tableId;
    private String transType;
    private List<BillDetailResponse> billDetailResponses;
}
