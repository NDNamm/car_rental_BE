package com.example.car_rental.controller.DashboardController;

import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.Booking.BookingChartResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.Booking.BookingStatisticsResponse;
import com.example.car_rental.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard/bookings")
@RequiredArgsConstructor
public class BookingDashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ApiResponse<BookingStatisticsResponse> bookingStats() {
        return ApiResponse.<BookingStatisticsResponse>builder()
                .code(200)
                .data(dashboardService.getBookingStats())
                .build();
    }

    @GetMapping("/day")
    public ApiResponse<List<BookingChartResponse>> bookingDailyChart() {
        return ApiResponse.<List<BookingChartResponse>>builder()
                .code(200)
                .data(dashboardService.getBookingDailyChart())
                .build();
    }

    @GetMapping("/month")
    public ApiResponse<List<BookingChartResponse>> bookingMonthlyChart() {
        return ApiResponse.<List<BookingChartResponse>>builder()
                .code(200)
                .data(dashboardService.getBookingMonthlyChart())
                .build();
    }

    @GetMapping("/year")
    public ApiResponse<List<BookingChartResponse>> bookingYearChart() {
        return ApiResponse.<List<BookingChartResponse>>builder()
                .code(200)
                .data(dashboardService.getBookingYearlyChart())
                .build();
    }

}