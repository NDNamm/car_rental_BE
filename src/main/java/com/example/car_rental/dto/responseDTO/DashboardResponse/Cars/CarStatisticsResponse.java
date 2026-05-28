package com.example.car_rental.dto.responseDTO.DashboardResponse.Cars;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarStatisticsResponse {
    private Long totalCars;
    private Long availableCars;
    private Long rentedCars;
    private Long maintenanceCars;
}
