package com.example.car_rental.repository;


import com.example.car_rental.entity.Handovers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HandoverRepository extends JpaRepository<Handovers, Long> {
    @Query("""
        SELECT h
        FROM Handovers h
        WHERE h.bookingDetails.id = :bookingDetailId
    """)
    List<Handovers> findByBookingDetailId(@Param("bookingDetailId") Long bookingDetailId);
}
