package com.enigma.wmb_api.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillDetailRequest {
    private String menuId;
    private Float qty;
}
