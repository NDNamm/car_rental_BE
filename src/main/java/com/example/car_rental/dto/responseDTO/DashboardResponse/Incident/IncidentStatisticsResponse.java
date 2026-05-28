package com.example.car_rental.dto.responseDTO.DashboardResponse.Incident;

import com.example.car_rental.entity.enums.IncidentEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncidentStatisticsResponse {

    private Long totalIncidents;
    private BigDecimal totalDamageCost;
}