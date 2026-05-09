package com.example.car_rental.repository;

import com.example.car_rental.entity.Bookings;
import com.example.car_rental.entity.Users;
import com.example.car_rental.entity.enums.BookingStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

//    @Query("""
//    SELECT u FROM Users u
//    WHERE (:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))
//      AND (:fullName IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :fullName, '%')))
//      AND (:phoneNumber IS NULL OR u.phoneNumber LIKE CONCAT('%', :phoneNumber, '%'))
//      AND (:role IS NULL OR u.role.roleName = :role)
//""")
//    Page<Users> finByUser(
//            @Param("email") String email,
//            @Param("fullName") String fullName,
//            @Param("phoneNumber") String phoneNumber,
//            @Param("role") String role,
//            Pageable pageable
//    );

    @Query("""
        SELECT u
        FROM Users u
        WHERE (:role IS NULL OR u.role.roleName = :role)
          AND (:isActive IS NULL OR u.isActive = :isActive)
          AND (
                :search IS NULL
                OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%'))
                OR u.phoneNumber LIKE CONCAT('%', :search, '%')
          )
    """)
    Page<Users> finByUser(
            @Param("search") String search,
            @Param("role") String role,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );

    Optional<Users> findByEmail(String email);
    boolean existsByEmail(String email);

}
