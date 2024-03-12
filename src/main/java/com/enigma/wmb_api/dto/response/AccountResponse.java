package com.enigma.wmb_api.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {
    private String id;
    private String email;
    private Boolean isAccountActive;
    private List<String> roles;
}
