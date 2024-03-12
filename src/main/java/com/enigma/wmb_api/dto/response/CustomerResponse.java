package com.enigma.wmb_api.dto.response;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {
    private String id;
    private String customerName;
    private String phoneNumber;
    private AccountResponse accountDetails;
}
