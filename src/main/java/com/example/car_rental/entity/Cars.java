package com.example.car_rental.entity;

import com.example.car_rental.entity.enums.CarStatusEnum;
import com.example.car_rental.entity.enums.GenderEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
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

    @Column(name = "brand")
    private String brand;

    @Column(name = "license_plate")
    private String licensePlate;

    @Column(name = "price_per_day")
    private BigDecimal pricePerDay;

    @Column(name = "deposit_amount")
    private BigDecimal depositAmount;

    @Column(name = "status")
    private CarStatusEnum status;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "category_id")
    private Categories category;

    @OneToMany(mappedBy = "car")
    @JsonIgnore
    private List<CarImages> carImages;
}
