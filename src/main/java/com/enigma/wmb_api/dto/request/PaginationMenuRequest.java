package com.enigma.wmb_api.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginationMenuRequest {
    private Integer page;
    private Integer size;
    private String sortBy;
    private String direction;
    private String menuName;
    private Integer minPrice;
    private Integer maxPrice;
}
