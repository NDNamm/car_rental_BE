package com.example.car_rental.controller.DashboardController;

import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.BookingDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse;
import com.example.car_rental.service.DashboardService.BookingDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard/bookings")
public class BookingDashboardController {

    private final BookingDashboardService bookingDashboardService;

    @GetMapping("/daily")
    public ApiResponse<List<BookingDashboardResponse>> getBookingByDay() {

        ApiResponse<List<BookingDashboardResponse>> apiResponse = new ApiResponse<>();
        LocalDateTime start = LocalDate.now().minusDays(7).atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(23, 59, 59);
        apiResponse.setData(bookingDashboardService.getBookingByDay(start, end));
        return apiResponse;
    }

    @GetMapping("/month")
    public ApiResponse<List<BookingDashboardResponse>> getBookingByMonth(@RequestParam int year) {

        ApiResponse<List<BookingDashboardResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(bookingDashboardService.getBookingByMonth(year));
        return apiResponse;
    }

    @GetMapping("/year")
    public ApiResponse<List<BookingDashboardResponse>> getBookingByYear() {

        ApiResponse<List<BookingDashboardResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(bookingDashboardService.getBookingByYear());
        return apiResponse;
    }

    @GetMapping("/status-count")
    public ApiResponse<List<StatusCountResponse>> getBookingStatusCount() {

        ApiResponse<List<StatusCountResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(bookingDashboardService.getBookingStatusCount());
        return apiResponse;
    }

    @GetMapping("/cancelled")
    public ApiResponse<Long> getCancelledBookings() {

        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setData(bookingDashboardService.getCancelledBookings());
        return apiResponse;
    }

    @GetMapping("/total")
    public ApiResponse<BigDecimal> getTotalBookings() {

        ApiResponse<BigDecimal> apiResponse = new ApiResponse<>();
        apiResponse.setData(bookingDashboardService.getTotalBookings());
        return apiResponse;
    }
}