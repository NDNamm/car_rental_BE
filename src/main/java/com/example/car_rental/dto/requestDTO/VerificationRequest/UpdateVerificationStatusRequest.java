package com.example.car_rental.dto.requestDTO.VerificationRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVerificationStatusRequest {
    private String status;
    private String note;
}