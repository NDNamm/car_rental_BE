package com.example.car_rental.repository.DashboardRepository;

import com.example.car_rental.dto.responseDTO.DashboardResponse.Booking.BookingChartResponse;
import com.example.car_rental.entity.Bookings;
import com.example.car_rental.entity.enums.BookingStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface BookingDashboardRepository extends JpaRepository<Bookings, Long> {

    Long countBy();
    Long countByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

    Long countByStatus(BookingStatusEnum status);

    @Query("""
    SELECT COALESCE(SUM(b.totalPrice),0)
    FROM Bookings b
    WHERE b.status <> 'CANCELLED'
""")
    BigDecimal getTotalAllRevenue();

    @Query("""
    SELECT COALESCE(SUM(b.totalPrice),0)
    FROM Bookings b
    WHERE b.status <> 'CANCELLED'
      AND b.createdAt BETWEEN :startDate AND :endDate
""")
    BigDecimal getTotalRevenue(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    Long countByStatusAndCreatedAtBetween(
            BookingStatusEnum status,
            LocalDateTime from,
            LocalDateTime to
    );

    @Query("""
    SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.Booking.BookingChartResponse(
        FUNCTION('DATE', b.createdAt),
        COUNT(b),
        COALESCE(SUM(b.totalPrice),0)
    )
    FROM Bookings b
    WHERE b.createdAt BETWEEN :startDate AND :endDate
      AND b.status = 'COMPLETED'
    GROUP BY FUNCTION('DATE', b.createdAt)
    ORDER BY FUNCTION('DATE', b.createdAt)
""")
    List<BookingChartResponse> getDailyChart(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
    SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.Booking.BookingChartResponse(
        MONTH(b.createdAt),
        COUNT(b),
        COALESCE(SUM(b.totalPrice),0)
    )
    FROM Bookings b
    WHERE b.createdAt BETWEEN :startDate AND :endDate
      AND b.status = 'COMPLETED'
    GROUP BY MONTH(b.createdAt), YEAR(b.createdAt)
    ORDER BY YEAR(b.createdAt), MONTH(b.createdAt)
""")
    List<BookingChartResponse> getMonthlyChart(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
    SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.Booking.BookingChartResponse(
        YEAR(b.createdAt),
        COUNT(b),
        COALESCE(SUM(b.totalPrice),0)
    )
    FROM Bookings b
    WHERE b.createdAt BETWEEN :startDate AND :endDate
      AND b.status = 'COMPLETED'
    GROUP BY YEAR(b.createdAt)
    ORDER BY YEAR(b.createdAt)
""")
    List<BookingChartResponse> getYearlyChart(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}