package com.example.car_rental.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "car_details")
public class CarDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seat_count")
    private int seatCount;

    @Column(name = "fuel_type")
    private String fuelType;

    @Column(name = "transmission")
    private String transmission;

    @Column(name = "manufacture_year")
    private int manufactureYear;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "delivery_available")
    private boolean deliveryAvailable;

    @Column(name = "delivery_fee")
    private BigDecimal deliveryFee;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "car_id")
    private Cars car;

    @ManyToMany
    @JoinTable(
            name = "car_features",
            joinColumns = @JoinColumn(name = "car_detail_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id")
    )
    private Set<Features> features = new HashSet<>();
}
