package com.example.car_rental.entity;

import com.example.car_rental.entity.enums.BookingStatusEnum;
import com.example.car_rental.entity.enums.InvoiceStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "invoices")
public class Invoices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rental_total")
    private BigDecimal rentalTotal;

    @Column(name = "penalty_total")
    private BigDecimal penaltyTotal;

    @Column(name = "note")
    private String note;

    @Column(name = "deposit_paid")
    private BigDecimal depositPaid;

    @Column(name = "final_amount")
    private BigDecimal finalAmount;

    @Column(name = "paid_total")
    private BigDecimal paidTotal;

    @Column(name = "remaining_amount")
    private BigDecimal remainingAmount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private InvoiceStatusEnum status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "booking_id")
    @JsonIgnore
    private Bookings booking;


}
