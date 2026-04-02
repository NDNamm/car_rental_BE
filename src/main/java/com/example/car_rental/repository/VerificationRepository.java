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
        Select v 
        From Verifications v 
        Join v.user u
        Where (:email is null or lower(u.email) Like lower(concat('%', :email, '%')))
          And (:fullName is null or lower(u.fullName) Like lower(concat('%', :fullName, '%')))
          And (:status is null or v.status = :status)
    """)
    Page<Verifications> findAllVerification(@Param("email") String email,
                                            @Param("fullName") String fullName,
                                            @Param("status") String status,
                                            Pageable pageable);

}
