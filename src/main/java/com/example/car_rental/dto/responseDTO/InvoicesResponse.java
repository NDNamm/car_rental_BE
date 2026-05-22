package com.example.car_rental.dto.responseDTO;

import com.example.car_rental.entity.enums.InvoiceStatusEnum;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoicesResponse {

    private Long id;
    private BigDecimal rentalTotal;
    private BigDecimal penaltyTotal;
    private String note;
    private BigDecimal depositPaid;
    private BigDecimal finalAmount;
    private BigDecimal paidTotal;
    private BigDecimal remainingAmount;
    private InvoiceStatusEnum status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long bookingId;
    private String bookingCode;
    private String customerName;
    private String customerEmail;
    private String customerPhone;

}
