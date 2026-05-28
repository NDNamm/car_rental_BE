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
                .completedBookings(bookingRepo.countByStatus(BookingStatusEnum.COMPLETED))
                .cancelledBookings(bookingRepo.countByStatus(BookingStatusEnum.CANCELLED))
                .pendingBookings(bookingRepo.countByStatus(BookingStatusEnum.PENDING))
                .totalRevenue(bookingRepo.getTotalRevenue())
                .build();
    }

    @Override
    public List<BookingChartResponse> getBookingChart() {
        return bookingRepo.getDailyChart();
    }

    @Override
    public CarStatisticsResponse getCarStats() {
        return CarStatisticsResponse.builder()
                .totalCars(carRepo.countBy())
                .availableCars(carRepo.countByStatus(CarStatusEnum.AVAILABLE))
                .availableCars(carRepo.countByStatus(CarStatusEnum.RENTED))
                .availableCars(carRepo.countByStatus(CarStatusEnum.MAINTENANCE))
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
    public List<InvoiceChartResponse> getInvoiceChart() {
        return invoiceRepo.getDailyChart();
    }

    @Override
    public IncidentStatisticsResponse getIncidentStats() {
        return IncidentStatisticsResponse.builder()
                .totalIncidents(incidentRepo.countBy())
                .totalDamageCost(incidentRepo.getTotalDamageCost())
                .build();
    }

    @Override
    public List<IncidentChartResponse> getIncidentChart() {
        return incidentRepo.getDailyChart();
    }

    @Override
    public List<IncidentTopResponse> getTopIncidents() {
        return incidentRepo.getTopIncidentsByFrequency();
    }
}