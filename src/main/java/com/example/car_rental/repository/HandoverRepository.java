package com.example.car_rental.repository;


import com.example.car_rental.entity.Handovers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HandoverRepository extends JpaRepository<Handovers, Long> {
    Optional<Handovers> findByBookingDetails_Id(Long bookingDetailId);
}
