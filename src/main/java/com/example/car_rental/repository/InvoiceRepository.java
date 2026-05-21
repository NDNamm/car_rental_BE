package com.example.car_rental.repository;

import com.example.car_rental.entity.Bookings;
import com.example.car_rental.entity.Invoices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoices, Long> {

    Optional<Invoices> findByBooking(Bookings bookings);
}
