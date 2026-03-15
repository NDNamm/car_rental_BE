package com.example.car_rental.repository;

import com.example.car_rental.entity.CarImages;
import com.example.car_rental.entity.Cars;
import com.example.car_rental.entity.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CarImagesRepository extends JpaRepository<CarImages, Long> {

}
