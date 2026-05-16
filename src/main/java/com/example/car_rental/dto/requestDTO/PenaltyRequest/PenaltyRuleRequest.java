package com.example.car_rental.dto.requestDTO.PenaltyRequest;

import com.example.car_rental.entity.enums.PenaltyStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PenaltyRuleRequest {
    private BigDecimal unitPrice;
    private String type;
    private String description;
    private Boolean isActive;
}
