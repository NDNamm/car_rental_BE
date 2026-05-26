package com.example.car_rental.repository.DashboardRepository;

import com.example.car_rental.dto.responseDTO.DashboardResponse.BookingDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.TopCustomerResponse;
import com.example.car_rental.entity.Bookings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingDashboardRepository extends JpaRepository<Bookings, Long> {

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

    @Query("""
                SELECT new  com.example.car_rental.dto.responseDTO.DashboardResponse.BookingDashboardResponse(
                    bd.car.name,
                    COUNT(bd.id)
                )
                FROM BookingDetails bd
                GROUP BY bd.car.name
                ORDER BY COUNT(bd.id) DESC
            """)
    Page<BookingDashboardResponse> getTopCars(Pageable pageable);

    @Query("SELECT COUNT(b) FROM Bookings b")
    Long getTotalBookings();

    @Query("""
            SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.TopCustomerResponse(
                b.user.fullName,
                COUNT(b.id),
                SUM(b.totalPrice)
            )
            FROM Bookings b
            GROUP BY b.user.fullName
            ORDER BY COUNT(b.id) DESC
            """)
    Page<TopCustomerResponse> getTopCustomers(Pageable pageable);
}
