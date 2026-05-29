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
@RequestMapping("/api/dashboard/incidents")
@RequiredArgsConstructor
public class IncidentDashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ApiResponse<IncidentStatisticsResponse> incidentStats() {
        return ApiResponse.<IncidentStatisticsResponse>builder()
                .code(200)
                .data(dashboardService.getIncidentStats())
                .build();
    }

    @GetMapping("/day")
    public ApiResponse<List<IncidentChartResponse>> getIncidentDailyChart() {
        return ApiResponse.<List<IncidentChartResponse>>builder()
                .code(200)
                .data(dashboardService.getIncidentDailyChart())
                .build();
    }

    @GetMapping("/month")
    public ApiResponse<List<IncidentChartResponse>> getIncidentMonthlyChart() {
        return ApiResponse.<List<IncidentChartResponse>>builder()
                .code(200)
                .data(dashboardService.getIncidentMonthlyChart())
                .build();
    }

    @GetMapping("t/year")
    public ApiResponse<List<IncidentChartResponse>> getIncidentYearlyChart() {
        return ApiResponse.<List<IncidentChartResponse>>builder()
                .code(200)
                .data(dashboardService.getIncidentYearlyChart())
                .build();
    }

    @GetMapping("/top")
    public ApiResponse<List<IncidentTopResponse>> getTopIncidents() {
        return ApiResponse.<List<IncidentTopResponse>>builder()
                .code(200)
                .data(dashboardService.getTopIncidents())
                .build();
    }
}