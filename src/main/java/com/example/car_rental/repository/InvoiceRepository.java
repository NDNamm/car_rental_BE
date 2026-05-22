package com.example.car_rental.repository;

import com.example.car_rental.entity.Bookings;
import com.example.car_rental.entity.Invoices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoices, Long> {

    Optional<Invoices> findByBookingId(Long bookingId);

    @Query("""
    SELECT i
    FROM Invoices i
    WHERE i.booking.id = :bookingId
    AND i.booking.user.email = :email
""")
    Optional<Invoices> findByBookingIdAndUserEmail(
            @Param("bookingId") Long bookingId,
            @Param("email") String email
    );
}
