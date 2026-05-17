package com.example.car_rental.dto.responseDTO;

import com.example.car_rental.entity.enums.PenaltyStatusEnum;
import com.example.car_rental.entity.enums.SourceEnum;
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
public class IncidentResponse {
    private Long id;
    private BigDecimal amount;
    private PenaltyStatusEnum status;
    private SourceEnum source;
    private LocalDateTime createdAt;
}
