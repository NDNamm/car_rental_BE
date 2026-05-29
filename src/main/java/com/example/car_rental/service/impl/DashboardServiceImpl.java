package com.example.car_rental.service.impl;

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
import com.example.car_rental.entity.enums.BookingStatusEnum;
import com.example.car_rental.entity.enums.CarStatusEnum;
import com.example.car_rental.entity.enums.InvoiceStatusEnum;
import com.example.car_rental.repository.DashboardRepository.*;
import com.example.car_rental.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final BookingDashboardRepository bookingRepo;
    private final CarDashboardRepository carRepo;
    private final CustomerDashboardRepository customerRepo;
    private final InvoiceDashboardRepository invoiceRepo;
    private final IncidentDashboardRepository incidentRepo;

    @Override
    public BookingStatisticsResponse getBookingStats() {

        return BookingStatisticsResponse.builder()
                .totalBookings(bookingRepo.countBy())
                .pendingBookings(bookingRepo.countByStatus(BookingStatusEnum.PENDING))
                .confirmedBookings(bookingRepo.countByStatus(BookingStatusEnum.CONFIRMED))
                .rentedBookings(bookingRepo.countByStatus(BookingStatusEnum.RENTED))
                .returningBookings(bookingRepo.countByStatus(BookingStatusEnum.RETURNING))
                .completedBookings(bookingRepo.countByStatus(BookingStatusEnum.COMPLETED))
                .cancelledBookings(bookingRepo.countByStatus(BookingStatusEnum.CANCELLED))
                .refundBookings(bookingRepo.countByStatus(BookingStatusEnum.REFUND))
                .totalRevenue(bookingRepo.getTotalRevenue())
                .build();
    }



    @Override
    public List<BookingChartResponse> getBookingDailyChart() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        return bookingRepo.getDailyChart(sevenDaysAgo);
    }

    @Override
    public List<BookingChartResponse> getBookingMonthlyChart() {
        return bookingRepo.getMonthlyChart();
    }

    @Override
    public List<BookingChartResponse> getBookingYearlyChart() {
        return bookingRepo.getYearlyChart();
    }



    @Override
    public CarStatisticsResponse getCarStats() {
        return CarStatisticsResponse.builder()
                .totalCars(carRepo.countBy())
                .availableCars(carRepo.countByStatus(CarStatusEnum.AVAILABLE))
                .rentedCars(carRepo.countByStatus(CarStatusEnum.RENTED))
                .maintenanceCars(carRepo.countByStatus(CarStatusEnum.MAINTENANCE))
                .build();
    }

    @Override
    public List<CarTopResponse> getCarTop() {
        return carRepo.getTopCars();
    }

    @Override
    public CustomerStatisticsResponse getCustomerStats() {
        return CustomerStatisticsResponse.builder()
                .totalCustomers(customerRepo.getTotalCustomers())
                .isActive(customerRepo.countActiveUsers())
                .inactiveCustomers(customerRepo.countInactiveUsers())
                .build();
    }

    @Override
    public List<CustomerTopResponse> getTopSpendingCustomers() {
        return customerRepo.getTopSpendingCustomers();
    }

    @Override
    public List<CustomerTopResponse> getTopCancelledCustomers() {
        return customerRepo.getTopCancelledCustomers();
    }

    @Override
    public InvoiceStatisticsResponse getInvoiceStats() {
        return InvoiceStatisticsResponse.builder()
                .totalInvoices(invoiceRepo.countBy())
                .totalRevenue(invoiceRepo.getTotalRevenue())
                .cancelInvoices(invoiceRepo.countByStatus(InvoiceStatusEnum.CANCELLED))
                .pendingInvoices(invoiceRepo.countByStatus(InvoiceStatusEnum.PENDING))
                .partialInvoices(invoiceRepo.countByStatus(InvoiceStatusEnum.PARTIAL))
                .paidInvoices(invoiceRepo.countByStatus(InvoiceStatusEnum.PAID))
                .build();
    }

    @Override
    public List<InvoiceChartResponse> getInvoiceDailyChart() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        return invoiceRepo.getDailyChart(sevenDaysAgo);
    }

    @Override
    public List<InvoiceChartResponse> getInvoiceMonthlyChart() {
        return invoiceRepo.getMonthlyChart();
    }

    @Override
    public List<InvoiceChartResponse> getInvoiceYearlyChart() {
        return invoiceRepo.getYearlyChart();
    }


    @Override
    public IncidentStatisticsResponse getIncidentStats() {
        return IncidentStatisticsResponse.builder()
                .totalIncidents(incidentRepo.countBy())
                .totalDamageCost(incidentRepo.getTotalDamageCost())
                .build();
    }

    @Override
    public List<IncidentChartResponse> getIncidentDailyChart() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        return incidentRepo.getDailyChart(sevenDaysAgo);
    }

    @Override
    public List<IncidentChartResponse> getIncidentMonthlyChart() {
        return incidentRepo.getMonthlyChart();
    }

    @Override
    public List<IncidentChartResponse> getIncidentYearlyChart() {
        return incidentRepo.getYearlyChart();
    }

    @Override
    public List<IncidentTopResponse> getTopIncidents() {
        return incidentRepo.getTopIncidentsByFrequency();
    }
}