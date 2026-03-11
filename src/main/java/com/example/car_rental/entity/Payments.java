package com.example.car_rental.entity;

import com.example.car_rental.entity.enums.BookingEnum;
import com.example.car_rental.entity.enums.PaymentMethodEnum;
import com.example.car_rental.entity.enums.PaymentStatusEnum;
import com.example.car_rental.entity.enums.PaymentTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "payment_method")
    private PaymentMethodEnum paymentMethodEnum;

    @Column(name = "payment_type")
    private PaymentTypeEnum paymentTypeEnum;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatusEnum status;

    @Column(name = "transaction_code")
    private String transactionCode;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "booking_id")
    private Bookings booking;

}
