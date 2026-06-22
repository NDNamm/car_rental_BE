package com.example.car_rental.entity;

import com.example.car_rental.entity.enums.VerStatusEnum;
import com.example.car_rental.entity.enums.LicenseStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "verifications")
public class Verifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cccd_front_url")
    private String cccdFrontUrl;

    @Column(name = "cccd_back_url")
    private String cccdBackUrl;

    @Column(name = "license_image_url")
    private String licenseImageUrl;

    @Column(name = "public_id_cccd_front")
    private String publicIdCccdFront;

    @Column(name = "public_id_cccd_back")
    private String publicIdCccdBack;

    @Column(name = "public_id_license")
    private String publicIdLicense;

    @Column(name = "license_number")
    private String licenseNumber;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private VerStatusEnum status;

    @Column(name = "note")
    private String note;

    @Column(name = "license_expired_date")
    private LocalDate licenseExpiredDate;

    @Column(name = "license_status")
    @Enumerated(EnumType.STRING)
    private LicenseStatusEnum licenseStatus;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    @JsonIgnore
    private Users user;
}
