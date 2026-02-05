package com.example.car_rental.dto.requestDTO.authRequest;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    String email;
    String password;
}
