package com.example.car_rental.dto.responseDTO.DashboardResponse.Booking;

import com.example.car_rental.entity.enums.BookingStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingStatisticsResponse {
    private Long totalBookings;
    private Long pendingBookings;
    private Long confirmedBookings;
    private Long rentedBookings;
    private Long returningBookings;
    private Long completedBookings;
    private Long cancelledBookings;
    private Long refundBookings;
    private BigDecimal totalRevenue;
}