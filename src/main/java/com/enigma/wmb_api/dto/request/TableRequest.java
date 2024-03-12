package com.enigma.wmb_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableRequest {
    @NotBlank(message = "table name is required")
    @Size(min = 3, max = 5)
    private String tableName;
}
