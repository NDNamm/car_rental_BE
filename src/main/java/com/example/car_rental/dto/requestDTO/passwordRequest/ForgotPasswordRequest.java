package com.example.car_rental.dto.requestDTO.passwordRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ForgotPasswordRequest {
    private String email;
    private String otp;
    @NotBlank(message = "PASSWORD_INVALID")
    @Size(min = 8, max = 20, message = "PASSWORD_INVALID")
    private String newPassword;
}
