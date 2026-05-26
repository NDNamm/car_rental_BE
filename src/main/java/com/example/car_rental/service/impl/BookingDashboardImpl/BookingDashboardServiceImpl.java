package com.example.car_rental.service.impl.BookingDashboardImpl;

import com.example.car_rental.dto.responseDTO.DashboardResponse.BookingDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.TopCustomerResponse;
import com.example.car_rental.repository.DashboardRepository.BookingDashboardRepository;
import com.example.car_rental.service.DashboardService.BookingDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingDashboardServiceImpl implements BookingDashboardService {

    private final BookingDashboardRepository bookingDashboardRepository;

    @Override
    public List<BookingDashboardResponse> getDailyRevenue(LocalDateTime from, LocalDateTime to) {
        return bookingDashboardRepository.getRevenueByDay(from, to);
    }

    @Override
    public List<BookingDashboardResponse> getMonthlyRevenue(int year) {
        return bookingDashboardRepository.getRevenueByMonth(year);
    }

    @Override
    public List<StatusCountResponse> getBookingStatusCount() {
        return bookingDashboardRepository.getBookingStatusCount();
    }

    @Override
    public Page<BookingDashboardResponse> getTopProduct(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookingDashboardRepository.getTopCars(pageable);
    }

    @Override
    public BigDecimal getTotalRevenue() {
        return bookingDashboardRepository.getTotalRevenue();
    }

    @Override
    public Long getTotalBookings() {
        return bookingDashboardRepository.getTotalBookings();
    }

    @Override
    public Page<TopCustomerResponse> getTopCustomers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookingDashboardRepository.getTopCustomers(pageable);
    }
}
