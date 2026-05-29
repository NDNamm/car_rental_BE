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

import java.util.List;

public interface DashboardService {

    // ================= BOOKING =================
    BookingStatisticsResponse getBookingStats();
    List<BookingChartResponse> getBookingDailyChart();
    List<BookingChartResponse> getBookingMonthlyChart();
    List<BookingChartResponse> getBookingYearlyChart();

    // ================= CAR =================
    CarStatisticsResponse getCarStats();
    List<CarTopResponse> getCarTop();

    // ================= CUSTOMER =================
    CustomerStatisticsResponse getCustomerStats();
    List<CustomerTopResponse> getTopSpendingCustomers();
    List<CustomerTopResponse> getTopCancelledCustomers();

    // ================= INVOICE =================
    InvoiceStatisticsResponse getInvoiceStats();
    List<InvoiceChartResponse> getInvoiceDailyChart();
    List<InvoiceChartResponse> getInvoiceMonthlyChart();
    List<InvoiceChartResponse> getInvoiceYearlyChart();


    // ================= INCIDENT =================
    IncidentStatisticsResponse getIncidentStats();
    List<IncidentChartResponse> getIncidentDailyChart();
    List<IncidentChartResponse> getIncidentMonthlyChart();
    List<IncidentChartResponse> getIncidentYearlyChart();
    List<IncidentTopResponse> getTopIncidents();
}
