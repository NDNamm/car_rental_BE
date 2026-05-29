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
@RequestMapping("/api/dashboard/invoices")
@RequiredArgsConstructor
public class InvoiceDashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ApiResponse<InvoiceStatisticsResponse> invoiceStats() {
        return ApiResponse.<InvoiceStatisticsResponse>builder()
                .code(200)
                .data(dashboardService.getInvoiceStats())
                .build();
    }

    @GetMapping("/day")
    public ApiResponse<List<InvoiceChartResponse>> invoiceDailyChart() {
        return ApiResponse.<List<InvoiceChartResponse>>builder()
                .code(200)
                .data(dashboardService.getInvoiceDailyChart())
                .build();
    }

    @GetMapping("/month")
    public ApiResponse<List<InvoiceChartResponse>> invoiceMonthlyChart() {
        return ApiResponse.<List<InvoiceChartResponse>>builder()
                .code(200)
                .data(dashboardService.getInvoiceMonthlyChart())
                .build();
    }

    @GetMapping("/year")
    public ApiResponse<List<InvoiceChartResponse>> invoiceYearlyChart() {
        return ApiResponse.<List<InvoiceChartResponse>>builder()
                .code(200)
                .data(dashboardService.getInvoiceYearlyChart())
                .build();
    }
}