package com.example.car_rental.repository;

import com.example.car_rental.entity.Bookings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingsRepository extends JpaRepository<Bookings, Long> {
    @Query("""
                SELECT b FROM Bookings b
                WHERE (:name IS NULL OR LOWER(o.name) LIKE LOWER(CONCAT('%', :name, '%')))
                  AND (:phoneNumber IS NULL OR LOWER(b.phoneNumber) LIKE LOWER(CONCAT('%', :phoneNumber, '%')))
                  AND (:bookingCode IS NULL OR LOWER(b.bookingCode) LIKE LOWER(CONCAT('%', :bookingCode, '%')))
                  AND (:status IS NULL OR LOWER(b.status) LIKE LOWER(CONCAT('%', :status, '%')))
            """)
    Page<Bookings> searchBookings(
            @Param("bookingCode") String bookingCode,
            @Param("name") String name,
            @Param("phoneNumber") String phoneNumber,
            @Param("status") String status,
            Pageable pageable
    );
}
