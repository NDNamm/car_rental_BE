package com.example.car_rental.dto.responseDTO.PenaltyResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PenaltyRuleResponse {
    private Long id;
    private BigDecimal unitPrice;
    private String type;
    private String description;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
