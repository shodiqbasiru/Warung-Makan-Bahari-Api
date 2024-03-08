package com.enigma.wmb_api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuRequest {
    private String id;

    @NotBlank(message = "menu name is required")
    @Size(min = 1, max = 100)
    private String menuName;

    @NotNull(message = "price is required")
    @Min(value = 0)
    private Integer price;

    private MultipartFile image;
}
