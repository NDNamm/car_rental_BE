package com.example.car_rental.dto.requestDTO;

import com.example.car_rental.entity.enums.PaymentTypeEnum;
import com.example.car_rental.entity.enums.RefundStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefundRequest {

    private String reason;
    private String note;
    private RefundStatus status;
}
