package com.example.car_rental.repository;

import com.example.car_rental.entity.Incidents;
import com.example.car_rental.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IncidentRepository extends JpaRepository<Incidents, Long> {
}
