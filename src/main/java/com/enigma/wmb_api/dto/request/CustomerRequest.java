package com.enigma.wmb_api.dto.request;

import com.enigma.wmb_api.validation.IndonesiaPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRequest {
    @NotBlank(message = "name is required")
    @Size(min = 3, max = 50)
    private String customerName;

    @NotNull(message = "phone number is required")
    @IndonesiaPhoneNumber
    private String phoneNumber;
}
