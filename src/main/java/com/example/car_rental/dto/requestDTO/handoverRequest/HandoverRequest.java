package com.example.car_rental.dto.requestDTO.handoverRequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HandoverRequest {
    private Integer mileage;
    private Integer fuelLevel;
    private String noteHandover;
}
