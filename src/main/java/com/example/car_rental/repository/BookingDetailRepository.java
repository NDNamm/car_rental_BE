package com.example.car_rental.repository;

import com.example.car_rental.dto.responseDTO.BookingResponse.BookingTimeResponse;
import com.example.car_rental.entity.BookingDetails;
import com.example.car_rental.entity.Bookings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetails, Long> {

    @Query("""
    SELECT COUNT(bd) FROM BookingDetails bd
    WHERE bd.car.id = :carId
    AND bd.booking.id <> :currentBookingId
    AND bd.booking.status <> 'CANCELLED'
    AND :startDate < bd.endDate
    AND bd.startDate < :endDate
""")
    long countOverlappingBookings(
            @Param("carId") Long carId,
            @Param("currentBookingId") Long currentBookingId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


    @Query("""
    SELECT new com.example.car_rental.dto.responseDTO.BookingResponse.BookingTimeResponse(
        bd.startDate,
        bd.endDate
    )
    FROM BookingDetails bd
    WHERE bd.car.id = :carId
    AND bd.booking.status IN ('PENDING', 'CONFIRMED')
""")
    List<BookingTimeResponse> findBookedRanges(@Param("carId") Long carId);

}
