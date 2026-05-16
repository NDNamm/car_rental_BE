package com.example.car_rental.repository;

import com.example.car_rental.entity.Incidents;
import com.example.car_rental.entity.Penalties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PenaltyRepository extends JpaRepository<Penalties, Long> {
}
