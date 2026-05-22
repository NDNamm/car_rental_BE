package com.example.car_rental.entity;

import com.example.car_rental.entity.enums.CarHandoverEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "handovers")
public class Handovers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private CarHandoverEnum type;

    @Column(name = "handover_time")
    private LocalDateTime handoverTime;

    @Column(name = "mileage")
    private Long mileage;

    @Column(name = "fuel_level")
    private Integer fuel_level;

    @Column(name = "note")
    private String note;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_detail_id")
    private BookingDetails bookingDetails;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Users staff;

    @OneToMany(mappedBy = "handovers", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<HandoverImages> handoverImages;

    @OneToMany(mappedBy = "handover",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Incidents> incidents = new ArrayList<>();

}
