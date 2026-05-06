package com.example.car_rental.entity;

import com.example.car_rental.entity.enums.BookingStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bookings")
public class Bookings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_code", unique = true, length = 20)
    private String bookingCode;

    @Column(name = "customer_phone")
    private String customerPhone;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "note")
    private String note;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "total_deposit_amount")
    private BigDecimal totalDepositAmount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookingStatusEnum status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "payment_deadline")
    private LocalDateTime paymentDeadline;

    @Column(name = "qr_code_url")
    private String qrCodeUrl;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payments payment;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<BookingDetails> bookingDetailsList;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<BookingStatusHistory> bookingStatusHistory;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Refunds refunds;

    @JsonIgnore
    @OneToOne(mappedBy = "booking")
    private Reviews review;

}
