package com.example.car_rental.dto.requestDTO.CarRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarFeatureRequest {
    private Long id;
    private String name;
}
