package com.example.car_rental.dto.responseDTO.DashboardResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDashboardResponse {

    private Object label;
    private BigDecimal totalRevenue;
    private Long totalBookings;
}
