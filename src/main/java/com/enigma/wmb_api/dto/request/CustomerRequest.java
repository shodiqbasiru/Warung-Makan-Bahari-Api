package com.enigma.wmb_api.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRequest {
    private String customerName;
    private String phoneNumber;
}
