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
@Table(name = "penalties")
public class Penalties {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "reason")
    private String reason;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PenaltyStatusEnum status;

    @Column(name = "source")
    @Enumerated(EnumType.STRING)
    private SourceEnum source;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id")
    private Incidents incident;

}
