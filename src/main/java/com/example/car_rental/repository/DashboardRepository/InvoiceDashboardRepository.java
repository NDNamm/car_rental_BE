package com.example.car_rental.repository.DashboardRepository;

import com.example.car_rental.dto.responseDTO.DashboardResponse.BookingDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse;
import com.example.car_rental.entity.Invoices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InvoiceDashboardRepository extends JpaRepository<Invoices, Long> {

    @Query("""
            SELECT SUM(b.totalPrice)
            FROM Bookings b
            WHERE b.status <> 'CANCELLED'
            """)
    BigDecimal getTotalRevenue();

    @Query("""
                  SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.BookingDashboardResponse(
                        MIN(b.createdAt),
                        SUM(b.totalPrice),
                        COUNT(b.id)
                  )
                  FROM Bookings b
                  WHERE b.createdAt BETWEEN :start AND :end
                  AND b.status <> 'CANCELLED'
                  GROUP BY FUNCTION('DATE', b.createdAt)
                  ORDER BY FUNCTION('DATE', b.createdAt)
            """)
    List<BookingDashboardResponse> getRevenueByDay(LocalDateTime start, LocalDateTime end);

    @Query("""
            SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.BookingDashboardResponse(MONTH(b.createdAt), SUM(b.totalPrice),  COUNT(b.id))
            FROM Bookings b
            WHERE YEAR(b.createdAt) = :year
            AND b.status <> 'CANCELLED'
            GROUP BY MONTH(b.createdAt)
            ORDER BY MONTH(b.createdAt)""")
    List<BookingDashboardResponse> getRevenueByMonth(int year);

    @Query("""
                SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse(
                    b.status,
                    COUNT(b)
                )
                FROM Bookings b
                GROUP BY b.status
            """)
    List<StatusCountResponse> getBookingStatusCount();

    @Query("SELECT COUNT(b) FROM Bookings b")
    Long getTotalBookings();

}
