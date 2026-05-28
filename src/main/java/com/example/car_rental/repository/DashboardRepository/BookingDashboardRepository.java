package com.example.car_rental.repository.DashboardRepository;

import com.example.car_rental.dto.responseDTO.DashboardResponse.Booking.BookingChartResponse;
import com.example.car_rental.entity.Bookings;
import com.example.car_rental.entity.enums.BookingStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface BookingDashboardRepository extends JpaRepository<Bookings, Long> {

    Long countBy();

    Long countByStatus(BookingStatusEnum status);

    @Query("""
        SELECT COALESCE(SUM(b.totalPrice),0)
        FROM Bookings b
        WHERE b.status <> 'CANCELLED'
    """)
    BigDecimal getTotalRevenue();

    @Query("""
        SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.Booking.BookingChartResponse(
            FUNCTION('DATE', b.createdAt),
            COUNT(b),
            COALESCE(SUM(b.totalPrice),0)
        )
        FROM Bookings b
        GROUP BY FUNCTION('DATE', b.createdAt)
    """)
    List<BookingChartResponse> getDailyChart();
}