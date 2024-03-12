package com.enigma.wmb_api.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuResponse {
    private String id;
    private String menuName;
    private Integer price;
    private ImageResponse image;
}
