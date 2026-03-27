package com.example.car_rental.dto.requestDTO;

import com.example.car_rental.entity.enums.PaymentTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefundRequest {

    private String bookingCode;
    private BigDecimal amount;
    private Long hoursBefore;
    private int cancelPercent;
    private PaymentTypeEnum paymentType;
    private String publicId;
    private String qrCodeUrl;
    private String note;
    private String publicIdProof;
    private String adminProofImage;
    private LocalDateTime createdAt;
}
