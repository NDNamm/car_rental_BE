package com.example.car_rental.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews")
public class Reviews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rating")
    private int rating;

    @Column(name = "comment")
    private String comment;

    @Column(name = "reason")
    private String reason;

    @Column(name = "is_hidden")
    private Boolean isHidden;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "car_id")
    private Cars car;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Bookings booking;

}
