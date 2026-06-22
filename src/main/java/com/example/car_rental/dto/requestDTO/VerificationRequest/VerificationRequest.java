package com.example.car_rental.dto.requestDTO.VerificationRequest;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VerificationRequest {
    private String licenseNumber;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate licenseExpiredDate;

}
