package com.example.car_rental.repository;

import com.example.car_rental.entity.CarFeatures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureRepository extends JpaRepository<CarFeatures, Long> {
    boolean existsByName(String name);

}
