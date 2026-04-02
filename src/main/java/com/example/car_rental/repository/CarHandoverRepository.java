package com.example.car_rental.repository;

import com.example.car_rental.entity.CarHandovers;
import com.example.car_rental.entity.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarHandoverRepository extends JpaRepository<CarHandovers, Long> {

    Optional<CarHandovers> findByBookingDetails_Id(Long bookingDetailId);
}
