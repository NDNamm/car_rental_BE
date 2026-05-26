package com.example.car_rental.service.DashboardService;

import com.example.car_rental.dto.responseDTO.DashboardResponse.BookingDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingDashboardService {
    List<BookingDashboardResponse> getBookingByDay(LocalDateTime from, LocalDateTime to);
    List<BookingDashboardResponse> getBookingByMonth(int year);
    List<BookingDashboardResponse> getBookingByYear();
    List<StatusCountResponse> getBookingStatusCount();
    Long getCancelledBookings();
    BigDecimal getTotalBookings();
}
