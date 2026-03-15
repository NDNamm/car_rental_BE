package com.example.car_rental.repository;

import com.example.car_rental.entity.Cars;
import com.example.car_rental.entity.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Cars, Long> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);

    @Query("""
          select c from Cars c
          where (:name is null or LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
          """)
    Page<Cars> findByName(@Param("name") String name, Pageable pageable);
}
