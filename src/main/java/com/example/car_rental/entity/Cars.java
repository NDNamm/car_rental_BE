package com.example.car_rental.entity;

import com.example.car_rental.entity.enums.CarStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cars")
public class Cars {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "license_plate")
    private String licensePlate;

    @Column(name = "price_per_day")
    private BigDecimal pricePerDay;

    @Column(name = "deposit_amount")
    private BigDecimal depositAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CarStatusEnum status = CarStatusEnum.AVAILABLE;

    @Column(name = "description")
    private String description;

    @Column(name = "trip_count")
    private int tripCount;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "category_id")
    private Categories category;

    @OneToMany(mappedBy = "car")
    @JsonIgnore
    private List<CarImages> carImages;

    @OneToOne(mappedBy = "car", cascade = CascadeType.ALL)
    @JsonIgnore
    private CarDetail carDetail;
}
