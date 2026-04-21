package com.example.car_rental.dto.requestDTO.passwordRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePassRequest {

    String oldPassword;
    @NotBlank(message = "PASSWORD_INVALID")
    @Size(min = 8, max = 20, message = "PASSWORD_INVALID")
    private String newPassword;

    @NotBlank(message = "PASSWORD_INVALID")
    @Size(min = 8, max = 20, message = "PASSWORD_INVALID")
    String confirmNewPassword;
}
