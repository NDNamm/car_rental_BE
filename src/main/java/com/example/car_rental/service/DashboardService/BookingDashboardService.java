package com.example.car_rental.service.DashboardService;

import com.example.car_rental.dto.responseDTO.DashboardResponse.BookingDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.TopCustomerResponse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingDashboardService {
    List<BookingDashboardResponse> getDailyRevenue(LocalDateTime from, LocalDateTime to);
    List<BookingDashboardResponse> getMonthlyRevenue(int year);
    List<StatusCountResponse> getBookingStatusCount();
    Page<BookingDashboardResponse> getTopProduct(int page, int size);
    BigDecimal getTotalRevenue();
    Long getTotalBookings();
    Page<TopCustomerResponse> getTopCustomers(int page, int size);
}
