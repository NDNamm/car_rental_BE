package com.example.car_rental.repository;

import com.example.car_rental.entity.Users;
import com.example.car_rental.entity.Verifications;
import com.example.car_rental.entity.enums.VerStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationRepository extends JpaRepository<Verifications, Long> {
   Optional<Verifications> findByUserId(Long userId);
    @Query("""
    SELECT v FROM Verifications v
    JOIN v.user u
    WHERE (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))
      AND (:fullName IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :fullName, '%')))
      AND (:status IS NULL OR v.status = :status)
""")
    Page<Verifications> findAllVerification(
            @Param("email") String email,
            @Param("fullName") String fullName,
            @Param("status") VerStatusEnum status,
            Pageable pageable
    );

}
