package com.example.car_rental.dto.responseDTO;

import com.example.car_rental.entity.enums.LicenseStatusEnum;
import com.example.car_rental.entity.enums.VerStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationResponse {

    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String cccdFrontUrl;
    private String cccdBackUrl;
    private String licenseImageUrl;
    private String licenseNumber;
    private VerStatusEnum status;
    private String note;
    private LocalDate licenseExpiredDate;
    private LicenseStatusEnum licenseStatus;
    private LocalDate createdAt;

}
