package com.example.car_rental.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "car_handovers")
public class CarHandovers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reason")
    private String reason;

    @Column(name = "handover_time")
    private LocalDateTime handoverTime;

    @Column(name = "return_time")
    private LocalDateTime returnTime;

    @Column(name = "car_condition_before")
    private String carConditionBefore;

    @Column(name = "car_condition_after")
    private String carConditionAfter;

    @Column(name = "note")
    private String note;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "booking_detail_id")
    private BookingDetails bookingDetails;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "staff_id")
    private Users staff;

}
