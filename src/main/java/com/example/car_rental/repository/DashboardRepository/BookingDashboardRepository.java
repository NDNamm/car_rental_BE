package com.example.car_rental.repository.DashboardRepository;

import com.example.car_rental.dto.responseDTO.DashboardResponse.BookingDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse;
import com.example.car_rental.entity.Bookings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingDashboardRepository extends JpaRepository<Bookings, Long> {

    @Query("""
        SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.BookingDashboardResponse(
            FUNCTION('DATE', b.createdAt),
            SUM(b.totalPrice),
            COUNT(b.id)
        )
        FROM Bookings b
        WHERE b.createdAt BETWEEN :from AND :to
        AND b.status <> 'CANCELLED'
        GROUP BY FUNCTION('DATE', b.createdAt)
        ORDER BY FUNCTION('DATE', b.createdAt)
        """)
    List<BookingDashboardResponse> getBookingByDay(
            LocalDateTime from,
            LocalDateTime to
    );

    @Query("""
        SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.BookingDashboardResponse(
            MONTH(b.createdAt),
            SUM(b.totalPrice),
            COUNT(b.id)
        )
        FROM Bookings b
        WHERE YEAR(b.createdAt) = :year
        AND b.status <> 'CANCELLED'
        GROUP BY MONTH(b.createdAt)
        ORDER BY MONTH(b.createdAt)
        """)
    List<BookingDashboardResponse> getBookingByMonth(int year);

    @Query("""
        SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.BookingDashboardResponse(
            YEAR(b.createdAt),
            SUM(b.totalPrice),
            COUNT(b.id)
        )
        FROM Bookings b
        WHERE b.status <> 'CANCELLED'
        GROUP BY YEAR(b.createdAt)
        ORDER BY YEAR(b.createdAt)
        """)
    List<BookingDashboardResponse> getBookingByYear();

    @Query("""
            SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse(
                b.status,
                COUNT(b.id)
            )
            FROM Bookings b
            GROUP BY b.status
            """)
    List<StatusCountResponse> getBookingStatusCount();

    @Query("""
            SELECT COUNT(b)
            FROM Bookings b
            WHERE b.status = 'CANCELLED'
            """)
    Long getCancelledBookings();

    @Query("""
       SELECT SUM(b.totalPrice)
       FROM Bookings b
       WHERE b.status <> 'CANCELLED'
       """)
    BigDecimal getTotalRevenue();
}