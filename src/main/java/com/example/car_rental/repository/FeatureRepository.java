package com.example.car_rental.repository;

import com.example.car_rental.entity.Features;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureRepository extends JpaRepository<Features, Long> {
    boolean existsByName(String name);

    @Query("""
          select f from Features f
          where (:name is null or LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%')))
          """)
    Page<Features> findByFeature(@Param("name") String name, Pageable pageable);
}
