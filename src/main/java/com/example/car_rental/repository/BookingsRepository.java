package com.example.car_rental.repository;

import com.example.car_rental.entity.Bookings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BookingsRepository extends JpaRepository<Bookings, Long> {
    @Query("""
                SELECT b FROM Bookings b
                WHERE (:name IS NULL OR LOWER(o.name) LIKE LOWER(CONCAT('%', :name, '%')))
                  AND (:phoneNumber IS NULL OR LOWER(b.phoneNumber) LIKE LOWER(CONCAT('%', :phoneNumber, '%')))
                  AND (:bookingCode IS NULL OR LOWER(b.bookingCode) LIKE LOWER(CONCAT('%', :bookingCode, '%')))
                  AND (:status IS NULL OR LOWER(b.status) LIKE LOWER(CONCAT('%', :status, '%')))
            """)
    Page<Bookings> searchBookings(
            @Param("bookingCode") String bookingCode,
            @Param("name") String name,
            @Param("phoneNumber") String phoneNumber,
            @Param("status") String status,
            Pageable pageable
    );

    @Query("""
                SELECT b FROM Bookings b WHERE b.user.email = :email
                  AND (:bookingCode IS NULL OR LOWER(b.bookingCode) LIKE LOWER(CONCAT('%', :bookingCode, '%')))
                  AND (:status IS NULL OR LOWER(b.status) LIKE LOWER(CONCAT('%', :status, '%')))
            """)
    Page<Bookings> findBookingsByUser(
            @Param("email") String email,
            @Param("bookingCode") String bookingCode,
            @Param("status") String status,
            Pageable pageable);

    @Query("SELECT COUNT(bd) FROM BookingDetails bd " +
                  "WHERE bd.car.id = :carId " +
                  "AND bd.booking.id <> :currentBookingId " + // Loại trừ chính đơn hàng đang sửa
                  "AND bd.booking.status <> 'CANCELLED' " +   // Không tính các đơn đã hủy
                  "AND :startDate <= bd.endDate " +
                  "AND bd.startDate <= :endDate")
    long countOverlappingBookings(@Param("carId") Long carId,
                                  @Param("currentBookingId") Long currentBookingId,
                                  @Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate);
}
