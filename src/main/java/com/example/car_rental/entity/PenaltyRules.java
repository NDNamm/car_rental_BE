package com.example.car_rental.entity;

import com.example.car_rental.entity.enums.PenaltyStatusEnum;
import com.example.car_rental.entity.enums.SourceEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "penalty_rules")
public class PenaltyRules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
