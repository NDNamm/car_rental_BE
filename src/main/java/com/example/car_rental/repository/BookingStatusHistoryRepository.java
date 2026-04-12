package com.example.car_rental.repository;

import com.example.car_rental.entity.BookingStatusHistory;
import com.example.car_rental.entity.Bookings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingStatusHistoryRepository extends JpaRepository<BookingStatusHistory, Long> {
}
