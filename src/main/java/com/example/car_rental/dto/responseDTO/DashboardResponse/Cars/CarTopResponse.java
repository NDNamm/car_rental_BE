package com.example.car_rental.dto.responseDTO.DashboardResponse.Cars;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarTopResponse {

    private String carName;
    private Long totalBookings;
    private BigDecimal revenue;

}
