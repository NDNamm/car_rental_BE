package com.example.car_rental.controller.DashboardController;

import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.Booking.BookingChartResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.Booking.BookingStatisticsResponse;
import com.example.car_rental.entity.enums.DashboardGroupBy;
import com.example.car_rental.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    @GetMapping("/stats/range")
    public ApiResponse<BookingStatisticsResponse> getStatsByRange(
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate from,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate to) {

        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(LocalTime.MAX);
        return ApiResponse.<BookingStatisticsResponse>builder()
                .code(200)
                .data(dashboardService.getBookingStats(start, end))
                .build();
    }

    @GetMapping("/chart")
    public ApiResponse<List<BookingChartResponse>> bookingChart(
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate from,
            @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate to,
            @RequestParam DashboardGroupBy groupBy) {

        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(LocalTime.MAX);
        return ApiResponse.<List<BookingChartResponse>>builder()
                .code(200)
                .data(dashboardService.getBookingChart(start, end, groupBy))
                .build();
    }
}