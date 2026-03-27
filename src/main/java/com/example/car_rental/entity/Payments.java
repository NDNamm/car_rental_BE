package com.example.car_rental.entity;

import com.example.car_rental.entity.enums.PaymentMethodEnum;
import com.example.car_rental.entity.enums.PaymentStatusEnum;
import com.example.car_rental.entity.enums.PaymentTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Column(name = "amount", precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "payment_content")
    private String paymentContent;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethodEnum paymentMethod;

    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentTypeEnum paymentType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatusEnum status;

    @Column(name = "transaction_code", unique = true)
    private String transactionCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    private Bookings booking;

}