package com.example.car_rental.dto.requestDTO.VerificationRequest;

import com.example.car_rental.entity.enums.VerStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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
