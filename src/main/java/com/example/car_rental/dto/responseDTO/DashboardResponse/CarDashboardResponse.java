package com.example.car_rental.dto.responseDTO.DashboardResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CarDashboardResponse {
    private String carName;
    private Long totalCars;
    private BigDecimal revenue;

    public CarDashboardResponse(String carName, Long totalCars) {
        this.carName = carName;
        this.totalCars = totalCars;
    }

    public CarDashboardResponse(String carName, BigDecimal revenue) {
        this.carName = carName;
        this.revenue = revenue;
    }

}
