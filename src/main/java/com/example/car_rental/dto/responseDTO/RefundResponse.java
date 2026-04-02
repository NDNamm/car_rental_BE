package com.example.car_rental.dto.responseDTO;

import com.example.car_rental.entity.enums.PaymentTypeEnum;
import com.example.car_rental.entity.enums.RefundStatus;
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
public class RefundResponse {
    private Long id;
    private String bookingCode;
    private BigDecimal amount;
    private String reason;
    private Long hoursBefore;
    private int cancelPercent;
    private RefundStatus status;
    private String publicId;
    private String qrCodeUrl;
    private String note;
    private String publicIdProof;
    private String adminProofImage;
    private LocalDateTime createdAt;
}
