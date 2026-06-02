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

    @GetMapping("/stats/range")
    public ApiResponse<IncidentStatisticsResponse> incidentStats(@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate from,
                                                                 @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate to) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(LocalTime.MAX);
        return ApiResponse.<IncidentStatisticsResponse>builder()
                .code(200)
                .data(dashboardService.getIncidentStats(start, end))
                .build();
    }

    @GetMapping("/chart")
    public ApiResponse<List<IncidentChartResponse>> getIncidentChart(@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate from,
                                                                          @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate to,
                                                                          @RequestParam DashboardGroupBy groupBy) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(LocalTime.MAX);
        return ApiResponse.<List<IncidentChartResponse>>builder()
                .code(200)
                .data(dashboardService.getIncidentChart(start, end, groupBy))
                .build();
    }

    @GetMapping("/top")
    public ApiResponse<List<IncidentTopResponse>> getTopIncidents(@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate from,
                                                                  @RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate to) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(LocalTime.MAX);
        return ApiResponse.<List<IncidentTopResponse>>builder()
                .code(200)
                .data(dashboardService.getTopIncidents(start, end))
                .build();
    }
}