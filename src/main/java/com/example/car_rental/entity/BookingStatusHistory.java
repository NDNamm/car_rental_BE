package com.example.car_rental.entity;

import com.example.car_rental.entity.enums.BookingStatusEnum;
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
@Table(name = "booking_status_history")
public class BookingStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "old_status")
    @Enumerated(EnumType.STRING)
    private BookingStatusEnum oldStatus;

    @Column(name = "new_status")
    @Enumerated(EnumType.STRING)
    private BookingStatusEnum newStatus;

    @Column(name = "reason")
    private String reason;

    @Column(name = "note")
    private String note;

    @Column(name = "changed_at")
    private LocalDateTime changedAt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "changed_by")
    private Users changedBy;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "booking_id")
    private Bookings booking;

}
