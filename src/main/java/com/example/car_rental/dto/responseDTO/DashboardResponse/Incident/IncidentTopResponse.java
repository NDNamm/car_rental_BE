package com.example.car_rental.dto.responseDTO.DashboardResponse.Incident;

import com.example.car_rental.entity.enums.IncidentEnum;
import com.example.car_rental.entity.enums.SourceEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncidentTopResponse   {

    private SourceEnum source;
    private IncidentEnum type;
    private Long count;
    private BigDecimal damageCost;

}