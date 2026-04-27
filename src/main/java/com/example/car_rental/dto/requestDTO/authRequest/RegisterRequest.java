package com.example.car_rental.dto.requestDTO.authRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "EMAIL_EXISTED")
    @Size(min = 5, max = 100, message = "EMAIL_EXISTED")
    private String email;

    @NotBlank(message = "FullNAME_INVALID")
    @Size(min = 5, max = 50, message = "FullNAME_INVALID")
    private String fullName;

    @NotBlank(message = "PASSWORD_INVALID")
    @Size(min = 8, max = 20, message = "PASSWORD_INVALID")
    private String password;

    @NotBlank(message = "PASSWORD_INVALID")
    @Size(min = 8, max = 20, message = "PASSWORD_INVALID")
    private String confirmPassword;

    private String roleName;
}
