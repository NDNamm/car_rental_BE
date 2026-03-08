package com.example.car_rental.dto.requestDTO;

import com.example.car_rental.entity.Users;
import com.example.car_rental.entity.enums.VerStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationRequest {

    private Long id;
    private Long userId;
    private String cccdFrontUrl;
    private String cccdBackUrl;
    private String licenseImageUrl;
    private String publicIdCccdFront;
    private String publicIdCccdBack;
    private String publicIdLicense;
    private String licenseNumber;
    private VerStatusEnum status;
    private String note;
    private String licenseExpiredDate;
    private LocalDate createdAt;

}
