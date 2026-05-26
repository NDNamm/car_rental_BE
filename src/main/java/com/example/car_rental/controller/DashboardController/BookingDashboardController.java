package com.example.car_rental.controller.DashboardController;

import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.BookingDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.TopCustomerResponse;
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

    @GetMapping("/revenue/daily")
    public ApiResponse<List<BookingDashboardResponse>> getDailyRevenue() {
        ApiResponse<List<BookingDashboardResponse>> apiResponse = new ApiResponse<>();
        LocalDateTime start = LocalDate.now().minusDays(7).atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(23, 59, 59);

        apiResponse.setData(bookingDashboardService.getDailyRevenue(start, end));

        return apiResponse;
    }

    @GetMapping("/revenue/month")
    public ApiResponse<List<BookingDashboardResponse>> getMonthlyRevenue(@RequestParam("year") int year) {
        ApiResponse<List<BookingDashboardResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(bookingDashboardService.getMonthlyRevenue(year));
        return apiResponse;
    }

    @GetMapping("/status-count")
    public ApiResponse<List<StatusCountResponse>> getOrderStatusCount() {
        ApiResponse<List<StatusCountResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(bookingDashboardService.getBookingStatusCount());
        return apiResponse;
    }

    @GetMapping("/cars/top-selling")
    public ApiResponse<Page<BookingDashboardResponse>> getTopProduct(@RequestParam(defaultValue = "0", required = false) int page,
                                                                     @RequestParam(defaultValue = "5") int size) {
        ApiResponse<Page<BookingDashboardResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(bookingDashboardService.getTopProduct(page, size));
        return apiResponse;
    }

    @GetMapping("/totalRevenue")
    public ApiResponse<BigDecimal> getTotalRevenue() {
        ApiResponse<BigDecimal> apiResponse = new ApiResponse<>();
        apiResponse.setData(bookingDashboardService.getTotalRevenue());
        return apiResponse;
    }

    @GetMapping("/totalBookings")
    public ApiResponse<Long> getTotalBookings() {
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setData(bookingDashboardService.getTotalBookings());
        return apiResponse;
    }

    @GetMapping("/customers/top")
    public ApiResponse<Page<TopCustomerResponse>> getTopCustomers(@RequestParam(defaultValue = "0", required = false) int page,
                                                                 @RequestParam(defaultValue = "5") int size) {
        ApiResponse<Page<TopCustomerResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(bookingDashboardService.getTopCustomers(page, size));
        return apiResponse;
    }


}
