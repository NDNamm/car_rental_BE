package com.example.car_rental.repository;

import com.example.car_rental.entity.Penalties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface PenaltyRepository extends JpaRepository<Penalties, Long> {
    @Query("""
    SELECT COALESCE(SUM(p.amount), 0)
    FROM Penalties p
    WHERE p.incident.handover.bookingDetails.booking.id = :bookingId
""")
    BigDecimal sumPenaltyByBookingId(@Param("bookingId") Long bookingId);
}
