package com.example.car_rental.repository;

import com.example.car_rental.entity.Bookings;
import com.example.car_rental.entity.Payments;
import com.example.car_rental.entity.Roles;
import com.example.car_rental.entity.enums.PaymentTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments, Long> {
    Optional<Payments> findByBookingAndPaymentType(Bookings bookings, PaymentTypeEnum status);

    Optional<Payments> findByBooking(Bookings booking);
}
