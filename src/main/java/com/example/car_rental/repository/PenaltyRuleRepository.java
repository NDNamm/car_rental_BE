package com.example.car_rental.repository;

import com.example.car_rental.entity.Penalties;
import com.example.car_rental.entity.PenaltyRules;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PenaltyRuleRepository extends JpaRepository<PenaltyRules, Long> {

    Optional<PenaltyRules> findByTypeAndIsActiveTrue(String type);
    @Query("""
    SELECT p FROM PenaltyRules p
    WHERE (:type IS NULL OR LOWER(p.type) LIKE LOWER(CONCAT('%', :type, '%')))
    AND (:isActive IS NULL OR p.isActive = :isActive)
""")
    Page<PenaltyRules> search(@Param("type") String type,
                              @Param("isActive") Boolean isActive,
                              Pageable pageable);
}
