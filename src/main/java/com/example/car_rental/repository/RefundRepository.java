package com.example.car_rental.repository;

import com.example.car_rental.dto.responseDTO.RefundResponse;
import com.example.car_rental.entity.Refunds;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RefundRepository extends JpaRepository<Refunds, Long> {

    @Query("""
          select r from Refunds r
          where (:bookingCode is null or LOWER(r.bookingCode) LIKE LOWER(CONCAT('%', :bookingCode, '%')))
          and (:status is null or LOWER(r.status) LIKE LOWER(CONCAT('%', :status, '%')))
          """)
    Page<Refunds> findByBookingCode(@Param("bookingCode") String bookingCode,
                                    @Param("status") String status,
                                    Pageable pageable);
}
