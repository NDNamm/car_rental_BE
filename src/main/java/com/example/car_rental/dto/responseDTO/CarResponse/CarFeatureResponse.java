package com.example.car_rental.dto.responseDTO.CarResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarFeatureResponse {
    private Long id;
    private String name;
}
