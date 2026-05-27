package com.example.car_rental.controller.DashboardController;

import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.BookingDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.InvoiceDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse;
import com.example.car_rental.service.DashboardService.BookingDashboardService;
import com.example.car_rental.service.DashboardService.InvoiceDashboardService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/dashboard/invoices")
public class InvoiceDashboardController {

    private final InvoiceDashboardService invoiceDashboardService;

    @GetMapping("/daily")
    public ApiResponse<List<InvoiceDashboardResponse>> getInvoiceByDay() {

        ApiResponse<List<InvoiceDashboardResponse>> apiResponse = new ApiResponse<>();
        LocalDateTime start = LocalDate.now().minusDays(7).atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(23, 59, 59);
        apiResponse.setData(invoiceDashboardService.getInvoiceByDay(start, end));
        return apiResponse;
    }

    @GetMapping("/month")
    public ApiResponse<List<InvoiceDashboardResponse>> getInvoiceByMonth(@RequestParam int year) {

        ApiResponse<List<InvoiceDashboardResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(invoiceDashboardService.getInvoiceByMonth(year));
        return apiResponse;
    }

    @GetMapping("/year")
    public ApiResponse<List<InvoiceDashboardResponse>> getInvoiceByYear() {

        ApiResponse<List<InvoiceDashboardResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(invoiceDashboardService.getInvoiceByYear());
        return apiResponse;
    }

    @GetMapping("/status-count")
    public ApiResponse<List<StatusCountResponse>> getInvoiceStatusCount() {

        ApiResponse<List<StatusCountResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(invoiceDashboardService.getInvoiceStatusCount());
        return apiResponse;
    }

    @GetMapping("/total")
    public ApiResponse<Long> getTotalInvoices() {

        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setData(invoiceDashboardService.getTotalInvoices());
        return apiResponse;
    }

    @GetMapping("/revenue")
    public ApiResponse<BigDecimal> getTotalRevenue() {

        ApiResponse<BigDecimal> apiResponse = new ApiResponse<>();
        apiResponse.setData(invoiceDashboardService.getTotalRevenue());
        return apiResponse;
    }

}