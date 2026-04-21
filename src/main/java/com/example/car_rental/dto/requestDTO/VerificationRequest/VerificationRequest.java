package com.example.car_rental.dto.requestDTO.VerificationRequest;

import com.example.car_rental.entity.enums.VerStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VerificationRequest {
    private String licenseNumber;
    private String licenseExpiredDate;

}
