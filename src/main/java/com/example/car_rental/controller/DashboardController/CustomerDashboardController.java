package com.example.car_rental.controller.DashboardController;

import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.Booking.BookingChartResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.Booking.BookingStatisticsResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.Cars.CarStatisticsResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.Cars.CarTopResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.Customer.CustomerStatisticsResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.Customer.CustomerTopResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.Incident.IncidentChartResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.Incident.IncidentStatisticsResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.Incident.IncidentTopResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.Invoice.InvoiceChartResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.Invoice.InvoiceStatisticsResponse;
import com.example.car_rental.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard/customers")
@RequiredArgsConstructor
public class CustomerDashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ApiResponse<CustomerStatisticsResponse> customerStats() {
        return ApiResponse.<CustomerStatisticsResponse>builder()
                .code(200)
                .data(dashboardService.getCustomerStats())
                .build();
    }

    @GetMapping("/spending")
    public ApiResponse<List<CustomerTopResponse>> getTopSpendingCustomers() {
        return ApiResponse.<List<CustomerTopResponse>>builder()
                .code(200)
                .data(dashboardService.getTopSpendingCustomers())
                .build();
    }

    @GetMapping("/cancelled")
    public ApiResponse<List<CustomerTopResponse>> getTopCancelledCustomers() {
        return ApiResponse.<List<CustomerTopResponse>>builder()
                .code(200)
                .data(dashboardService.getTopCancelledCustomers())
                .build();
    }

}