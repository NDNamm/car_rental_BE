package com.example.car_rental.repository;

import com.example.car_rental.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    @Query("""
    SELECT u FROM Users u
    WHERE (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))
      AND (:fullName IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :fullName, '%')))
      AND (:phoneNumber IS NULL OR u.phoneNumber LIKE CONCAT('%', :phoneNumber, '%'))
      AND (:role IS NULL OR u.role.roleName = :role)
""")
    Page<Users> finByUser(
            @Param("email") String email,
            @Param("fullName") String fullName,
            @Param("phoneNumber") String phoneNumber,
            @Param("role") String role,
            Pageable pageable
    );

    Optional<Users> findByEmail(String email);
    boolean existsByEmail(String email);

}
