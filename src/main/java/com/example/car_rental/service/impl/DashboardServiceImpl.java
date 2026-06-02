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
import com.example.car_rental.entity.enums.DashboardGroupBy;
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
                .totalRevenue(bookingRepo.getTotalAllRevenue())
                .build();
    }

    @Override
    public List<BookingChartResponse> getBookingChart(LocalDateTime from, LocalDateTime to, DashboardGroupBy groupBy) {
        return switch (groupBy) {
            case DAY -> bookingRepo.getDailyChart(from, to);
            case MONTH -> bookingRepo.getMonthlyChart(from, to);
            case YEAR -> bookingRepo.getYearlyChart(from, to);
        };
    }

    @Override
    public BookingStatisticsResponse getBookingStats(LocalDateTime from, LocalDateTime to) {
        return BookingStatisticsResponse.builder()
                .totalBookings(bookingRepo.countByCreatedAtBetween(from, to))
                .pendingBookings(bookingRepo.countByStatusAndCreatedAtBetween(
                        BookingStatusEnum.PENDING, from, to))
                .confirmedBookings(bookingRepo.countByStatusAndCreatedAtBetween(
                        BookingStatusEnum.CONFIRMED, from, to))
                .rentedBookings(bookingRepo.countByStatusAndCreatedAtBetween(
                        BookingStatusEnum.RENTED, from, to))
                .returningBookings(bookingRepo.countByStatusAndCreatedAtBetween(
                        BookingStatusEnum.RETURNING, from, to))
                .completedBookings(bookingRepo.countByStatusAndCreatedAtBetween(
                        BookingStatusEnum.COMPLETED, from, to))
                .cancelledBookings(bookingRepo.countByStatusAndCreatedAtBetween(
                        BookingStatusEnum.CANCELLED, from, to))
                .refundBookings(bookingRepo.countByStatusAndCreatedAtBetween(
                        BookingStatusEnum.REFUND, from, to))
                .totalRevenue(bookingRepo.getTotalRevenue(from, to))
                .build();
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
                .totalRevenue(invoiceRepo.getTotalAllRevenue())
                .cancelInvoices(invoiceRepo.countByStatus(InvoiceStatusEnum.CANCELLED))
                .pendingInvoices(invoiceRepo.countByStatus(InvoiceStatusEnum.PENDING))
                .partialInvoices(invoiceRepo.countByStatus(InvoiceStatusEnum.PARTIAL))
                .paidInvoices(invoiceRepo.countByStatus(InvoiceStatusEnum.PAID))
                .build();
    }

    @Override
    public List<InvoiceChartResponse> getInvoiceChart(LocalDateTime from, LocalDateTime to, DashboardGroupBy groupBy) {
        return switch (groupBy) {
            case DAY -> invoiceRepo.getDailyChart(from, to);
            case MONTH -> invoiceRepo.getMonthlyChart(from, to);
            case YEAR -> invoiceRepo.getYearlyChart(from, to);
        };
    }

    @Override
    public InvoiceStatisticsResponse getInvoiceStats(LocalDateTime from, LocalDateTime to) {
        return InvoiceStatisticsResponse.builder()
                .totalInvoices(invoiceRepo.countByCreatedAtBetween(from, to))
                .totalRevenue(invoiceRepo.getTotalRevenue(from, to))
                .cancelInvoices(invoiceRepo.countByStatusAndCreatedAtBetween(InvoiceStatusEnum.CANCELLED, from, to))
                .pendingInvoices(invoiceRepo.countByStatusAndCreatedAtBetween(InvoiceStatusEnum.PENDING, from, to))
                .partialInvoices(invoiceRepo.countByStatusAndCreatedAtBetween(InvoiceStatusEnum.PARTIAL, from, to))
                .paidInvoices(invoiceRepo.countByStatusAndCreatedAtBetween(InvoiceStatusEnum.PAID, from, to))
                .build();
    }


    @Override
    public IncidentStatisticsResponse getIncidentStats() {
        return IncidentStatisticsResponse.builder()
                .totalIncidents(incidentRepo.countBy())
                .totalDamageCost(incidentRepo.getTotalAllDamageCost())
                .build();
    }

    @Override
    public List<IncidentChartResponse> getIncidentChart(LocalDateTime from, LocalDateTime to, DashboardGroupBy groupBy) {
        return switch (groupBy) {
            case DAY -> incidentRepo.getDailyChart(from, to);
            case MONTH -> incidentRepo.getMonthlyChart(from, to);
            case YEAR -> incidentRepo.getYearlyChart(from, to);
        };
    }

    @Override
    public List<IncidentTopResponse> getTopIncidents(LocalDateTime from, LocalDateTime to) {
        return incidentRepo.getTopIncidentsByFrequency(from, to);
    }

    @Override
    public IncidentStatisticsResponse getIncidentStats(LocalDateTime from, LocalDateTime to) {
        return IncidentStatisticsResponse.builder()
                .totalIncidents(incidentRepo.countByCreatedAtBetween(from, to))
                .totalDamageCost(incidentRepo.getTotalDamageCost(from,to))
                .build();
    }


}