package com.example.car_rental.entity;

import com.example.car_rental.entity.enums.BookingEnum;
import com.example.car_rental.entity.enums.VerStatusEnum;
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
@Table(name = "bookings")
public class Bookings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "deposit_amount")
    private BigDecimal depositAmount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private BookingEnum status;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "car_id")
    private Cars car;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "staff_id")
    private Users userStaff;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payments payment;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private CarHandovers carHandovers;
}
