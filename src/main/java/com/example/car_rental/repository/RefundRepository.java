package com.example.car_rental.repository;

import com.example.car_rental.dto.responseDTO.RefundResponse;
import com.example.car_rental.entity.Bookings;
import com.example.car_rental.entity.Refunds;
import com.example.car_rental.entity.enums.RefundStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RefundRepository extends JpaRepository<Refunds, Long> {

    @Query("""
          select r from Refunds r
          where (:bookingCode is null or LOWER(r.bookingCode) LIKE LOWER(CONCAT('%', :bookingCode, '%')))
          and (:status is null or LOWER(r.status) LIKE LOWER(CONCAT('%', :status, '%')))
          """)
    Page<Refunds> findByBookingCode(@Param("bookingCode") String bookingCode,
                                    @Param("status") RefundStatus status,
                                    Pageable pageable);

    @Query("""
    SELECT r FROM Refunds r
    WHERE r.booking.user.email = :email
      AND (:bookingCode IS NULL OR LOWER(r.bookingCode) LIKE LOWER(CONCAT('%', :bookingCode, '%')))
      AND (:status IS NULL OR r.status = :status)
""")
    Page<Refunds> findRefundByUser(
            @Param("email") String email,
            @Param("bookingCode") String bookingCode,
            @Param("status") RefundStatus status,
            Pageable pageable);

    boolean existsByBooking(Bookings bookingId);

}
