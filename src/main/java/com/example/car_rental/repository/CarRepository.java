package com.example.car_rental.repository;

import com.example.car_rental.entity.Cars;
import com.example.car_rental.entity.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Cars, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    @Query("""
                SELECT c FROM Cars c
                JOIN c.carDetail cd
                WHERE (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
                  AND (:city IS NULL OR LOWER(cd.city) LIKE LOWER(CONCAT('%', :city, '%')))
                  AND (:status IS NULL OR LOWER(c.status) LIKE LOWER(CONCAT('%', :status, '%')))
            """)
    Page<Cars> findCars(
            @Param("name") String name,
            @Param("city") String city,
            @Param("status") String status,
            Pageable pageable
    );

    Page<Cars> findByCategoryId(Long categoryId, Pageable pageable);


    @Query("""
                SELECT DISTINCT cd.city
                FROM Cars c
                JOIN c.carDetail cd
                WHERE (
                    :city IS NULL
                    OR LOWER(cd.city) LIKE LOWER(CONCAT('%', :city, '%'))
                )
                AND cd.city IS NOT NULL
                ORDER BY cd.city ASC
            """)
    Page<String> findAllCities(
            @Param("city") String city,
            Pageable pageable
    );

    @Query("""
            SELECT c FROM Cars c
            WHERE c.category.id = :categoryId
            AND c.carDetail.city = :city
            AND c.id NOT IN (
                SELECT bd.car.id
                FROM BookingDetails bd
                WHERE bd.startDate < :endDate
                AND bd.endDate > :startDate
            )
            """)
    List<Cars> findAvailableSameCityCars(
            @Param("categoryId") Long categoryId,
            @Param("city") String city,
            @Param("startDate") LocalDateTime start,
            @Param("endDate") LocalDateTime end
    );
}
