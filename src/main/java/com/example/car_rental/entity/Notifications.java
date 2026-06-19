package com.example.car_rental.entity;

import com.example.car_rental.entity.enums.BookingStatusEnum;
import com.example.car_rental.entity.enums.NotificationTypeEnum;
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
@Table(name = "notifications")
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private NotificationTypeEnum type;

    @Column(name = "message")
    private String message;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "reference_id")
    private Long referenceId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private Users user;

}
