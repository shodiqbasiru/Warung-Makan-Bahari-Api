package com.enigma.wmb_api.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuRequest {
    private String menuName;
    private Float price;
}
