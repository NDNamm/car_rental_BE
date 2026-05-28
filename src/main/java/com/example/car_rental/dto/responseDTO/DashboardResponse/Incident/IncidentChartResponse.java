package com.example.car_rental.dto.responseDTO.DashboardResponse.Incident;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncidentChartResponse  {

    private Object label;
    private Long count;
    private BigDecimal revenue;


}