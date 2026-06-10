package com.example.car_rental.service;

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
import java.time.LocalDateTime;
import java.util.List;

public interface DashboardService {

    // ================= BOOKING =================
    BookingStatisticsResponse getBookingStats();

    List<BookingChartResponse> getBookingChart(LocalDateTime from, LocalDateTime to, DashboardGroupBy groupBy);

    BookingStatisticsResponse getBookingStats(LocalDateTime from, LocalDateTime to);

    // ================= CAR =================
    CarStatisticsResponse getCarStats();

    List<CarTopResponse> getCarTop();

    List<CarTopResponse> getCarTop(LocalDateTime from, LocalDateTime to);

    // ================= CUSTOMER =================
    CustomerStatisticsResponse getCustomerStats();

    List<CustomerTopResponse> getTopSpendingCustomers();

    List<CustomerTopResponse> getTopSpendingCustomers(LocalDateTime from, LocalDateTime to);

    List<CustomerTopResponse> getTopCancelledCustomers();

    List<CustomerTopResponse> getTopCancelledCustomers(LocalDateTime from, LocalDateTime to);

    // ================= INVOICE =================
    InvoiceStatisticsResponse getInvoiceStats();

    List<InvoiceChartResponse> getInvoiceChart(LocalDateTime from, LocalDateTime to, DashboardGroupBy groupBy);

    InvoiceStatisticsResponse getInvoiceStats(LocalDateTime from, LocalDateTime to);

    // ================= INCIDENT =================
    IncidentStatisticsResponse getIncidentStats();

    List<IncidentChartResponse> getIncidentChart(LocalDateTime from, LocalDateTime to, DashboardGroupBy groupBy);

    List<IncidentTopResponse> getTopIncidents(LocalDateTime from, LocalDateTime to);

    IncidentStatisticsResponse getIncidentStats(LocalDateTime from, LocalDateTime to);
}