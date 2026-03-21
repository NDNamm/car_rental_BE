package com.example.car_rental.dto.requestDTO.CarRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarImageRequest {
    private String imageUrl;
}
