package com.example.car_rental.service.impl.BookingDashboardImpl;

import com.example.car_rental.dto.responseDTO.DashboardResponse.BookingDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse;
import com.example.car_rental.repository.DashboardRepository.BookingDashboardRepository;
import com.example.car_rental.service.DashboardService.BookingDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingDashboardServiceImpl implements BookingDashboardService {

    private final BookingDashboardRepository bookingDashboardRepository;

    @Override
    public List<BookingDashboardResponse> getBookingByDay(LocalDateTime from, LocalDateTime to) {
        return bookingDashboardRepository.getBookingByDay(from, to);
    }

    @Override
    public List<BookingDashboardResponse> getBookingByMonth(int year) {
        return bookingDashboardRepository.getBookingByMonth(year);
    }

    @Override
    public List<BookingDashboardResponse> getBookingByYear() {
        return bookingDashboardRepository.getBookingByYear();
    }

    @Override
    public List<StatusCountResponse> getBookingStatusCount() {
        return bookingDashboardRepository.getBookingStatusCount();
    }

    @Override
    public Long getCancelledBookings() {
        return bookingDashboardRepository.getCancelledBookings();
    }

    @Override
    public BigDecimal getTotalBookings() {
        return bookingDashboardRepository.getTotalRevenue();
    }
}