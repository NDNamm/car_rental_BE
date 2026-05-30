package com.example.car_rental.repository;

import com.example.car_rental.entity.Bookings;
import com.example.car_rental.entity.enums.BookingStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingsRepository extends JpaRepository<Bookings, Long> {
//    @Query("""
//                SELECT b FROM Bookings b
//                WHERE (:customerEmail IS NULL OR LOWER(b.customerEmail) LIKE LOWER(CONCAT('%', :customerEmail, '%')))
//                  AND (:customerName IS NULL OR LOWER(b.customerName) LIKE LOWER(CONCAT('%', :customerName, '%')))
//                  AND (:customerPhone IS NULL OR LOWER(b.customerPhone) LIKE LOWER(CONCAT('%', :customerPhone, '%')))
//                  AND (:bookingCode IS NULL OR LOWER(b.bookingCode) LIKE LOWER(CONCAT('%', :bookingCode, '%')))
//                  AND (:status IS NULL OR b.status = :status)
//            """)
//    Page<Bookings> searchBookings(
//            @Param("bookingCode") String bookingCode,
//            @Param("customerEmail") String email,
//            @Param("customerName") String customerName,
//            @Param("customerPhone") String customerPhone,
//            @Param("status") BookingStatusEnum status,
//            Pageable pageable
//    );

    @Query("""
    SELECT b FROM Bookings b
    WHERE (:status IS NULL OR b.status = :status)
    AND (
        :search IS NULL OR
        LOWER(b.bookingCode) LIKE LOWER(CONCAT('%', :search, '%')) OR
        LOWER(b.customerEmail) LIKE LOWER(CONCAT('%', :search, '%')) OR
        LOWER(b.customerName) LIKE LOWER(CONCAT('%', :search, '%')) OR
        LOWER(b.customerPhone) LIKE LOWER(CONCAT('%', :search, '%'))
    )
""")
    Page<Bookings> searchBookings(
            @Param("search") String search,
            @Param("status") BookingStatusEnum status,
            Pageable pageable
    );

    @Query("""
    SELECT b FROM Bookings b 
    WHERE b.user.email = :email
      AND (:bookingCode IS NULL OR LOWER(b.bookingCode) LIKE LOWER(CONCAT('%', :bookingCode, '%')))
      AND (:status IS NULL OR b.status = :status)
""")
    Page<Bookings> findBookingsByUser(
            @Param("email") String email,
            @Param("bookingCode") String bookingCode,
            @Param("status") BookingStatusEnum status,
            Pageable pageable);

    Optional<Bookings> findByBookingCode(String bookingCode);

    List<Bookings> findByStatusAndPaymentDeadlineBefore(
            BookingStatusEnum status,
            LocalDateTime time
    );

    Bookings findByIdAndUserEmail(Long id, String email);

    Bookings findBookingById(Long bookingId);
}
