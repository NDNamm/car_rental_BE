package com.example.car_rental.dto.responseDTO.CarResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarImageResponse {
    private Long id;
    private String imageUrl;
}
