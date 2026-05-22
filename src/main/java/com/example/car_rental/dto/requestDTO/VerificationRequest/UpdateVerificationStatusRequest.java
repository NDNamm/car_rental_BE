package com.example.car_rental.dto.requestDTO.VerificationRequest;

import com.example.car_rental.entity.enums.VerStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVerificationStatusRequest {
    private VerStatusEnum status;
    private String note;
}