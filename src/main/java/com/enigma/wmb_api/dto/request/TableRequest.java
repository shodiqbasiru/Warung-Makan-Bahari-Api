package com.enigma.wmb_api.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableRequest {
    private String tableName;
}
