package com.example.car_rental.controller;

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
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/booking/stats")
    public BookingStatisticsResponse bookingStats() {
        return dashboardService.getBookingStats();
    }

    @GetMapping("/booking/chart")
    public List<BookingChartResponse> bookingChart() {
        return dashboardService.getBookingChart();
    }

    @GetMapping("/car/stats")
    public CarStatisticsResponse carStats() {
        return dashboardService.getCarStats();
    }

    @GetMapping("/car/top")
    public List<CarTopResponse> carTop() {
        return dashboardService.getCarTop();
    }

    @GetMapping("/customer/stats")
    public CustomerStatisticsResponse customerStats() {
        return dashboardService.getCustomerStats();
    }

    @GetMapping("/customer/spending")
    public List<CustomerTopResponse> getTopSpendingCustomers() {
        return dashboardService.getTopSpendingCustomers();
    }

    @GetMapping("/customer/cancelled")
    public List<CustomerTopResponse> getTopCancelledCustomers() {
        return dashboardService.getTopCancelledCustomers();
    }


    //invoice
    @GetMapping("/invoice/stats")
    public InvoiceStatisticsResponse invoiceStats() {
        return dashboardService.getInvoiceStats();
    }

    @GetMapping("/invoice/chart")
    public List<InvoiceChartResponse> invoiceChart() {
        return dashboardService.getInvoiceChart();
    }


    //INCIDENT
    @GetMapping("/incident/stats")
    public IncidentStatisticsResponse incidentStats() {
        return dashboardService.getIncidentStats();
    }

    @GetMapping("/incident/chart")
    public List<IncidentChartResponse> getIncidentChart() {
        return dashboardService.getIncidentChart();
    }

    @GetMapping("/incident/top")
    public List<IncidentTopResponse> getTopIncidents() {
        return dashboardService.getTopIncidents();
    }
}
