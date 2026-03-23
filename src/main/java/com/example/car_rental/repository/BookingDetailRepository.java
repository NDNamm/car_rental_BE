package com.example.car_rental.repository;

import com.example.car_rental.entity.BookingDetails;
import com.example.car_rental.entity.Bookings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetails, Long> {

    @Query("SELECT COUNT(bd) FROM BookingDetails bd " +
                  "WHERE bd.car.id = :carId " +
                  "AND bd.booking.id <> :currentBookingId " + // Loại trừ chính đơn hàng đang sửa
                  "AND bd.booking.status <> 'CANCELLED' " +   // Không tính các đơn đã hủy
                  "AND :startDate <= bd.endDate " +
                  "AND bd.startDate <= :endDate")
    long countOverlappingBookings(@Param("carId") Long carId,
                                  @Param("currentBookingId") Long currentBookingId,
                                  @Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate);
}
