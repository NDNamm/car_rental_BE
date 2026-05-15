package com.example.car_rental.repository;

import com.example.car_rental.entity.CarImages;
import com.example.car_rental.entity.HandoverImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HandoverImagesRepository extends JpaRepository<HandoverImages, Long> {

}
