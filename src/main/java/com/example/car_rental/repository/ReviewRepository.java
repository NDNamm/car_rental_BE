package com.example.car_rental.repository;

import com.example.car_rental.entity.Reviews;
import com.example.car_rental.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Reviews, Long> {
    boolean existsByBookingId(Long bookingId);
}
