package com.enigma.wmb_api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;
    private String customerId;
    private String tableId;
    private String transType;
    private List<BillDetailResponse> billDetailResponses;
    private PaymentResponse paymentResponse;
}
