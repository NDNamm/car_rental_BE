package com.example.car_rental.repository;

import com.example.car_rental.entity.Categories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, Long> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);

    @Query("""
          select c from Categories c
          where (:name is null or LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
          """)
    Page<Categories> findByName(@Param("name") String name, Pageable pageable);
}
