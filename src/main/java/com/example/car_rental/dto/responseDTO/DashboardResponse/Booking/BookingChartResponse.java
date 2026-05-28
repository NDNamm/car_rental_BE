package com.example.car_rental.dto.responseDTO.DashboardResponse.Booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingChartResponse {

    private Object label;
    private Long totalBookings;
    private BigDecimal revenue;

}
