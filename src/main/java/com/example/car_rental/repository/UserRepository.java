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
             Select u from Users u
             Where (:email is null or lower(u.email) Like Lower(concat('%',:email, '%')))
               And (:fullName is null or lower(u.fullName) Like Lower(concat('%', :fullName, '%')))
               And (:phoneNumber is null or(u.phoneNumber) Like Lower(concat('&', :phoneNumber, '%')))
            """)
    Page<Users> finByUser(@Param("email") String email,
                          @Param("fullName") String fullName,
                          @Param("phoneNumber") String phoneNumber,
                          Pageable pageable);

    Optional<Users> findByEmail(String email);
    boolean existsByEmail(String email);
}
