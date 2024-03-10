package com.enigma.wmb_api.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentItemDetailRequest {
    private String id;
    private Integer price;
    private Integer quantity;
    private String name;
}
