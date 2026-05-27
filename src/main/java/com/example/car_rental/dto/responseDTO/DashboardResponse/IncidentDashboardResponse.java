package com.example.car_rental.dto.responseDTO.DashboardResponse;

import com.example.car_rental.entity.enums.IncidentEnum;
import com.example.car_rental.entity.enums.SourceEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncidentDashboardResponse {

    private LocalDate date;
    private Object label;
    private BigDecimal revenue;
    private Long count;
    private SourceEnum sourceEnum;
    private IncidentEnum type;

    // DAY
    public IncidentDashboardResponse(java.sql.Date date, BigDecimal revenue, Long count) {
        this.date = date.toLocalDate();
        this.revenue = revenue;
        this.count = count;
    }

    // MONTH
    public IncidentDashboardResponse(Object label, BigDecimal revenue, Long count) {
        this.label = label;
        this.revenue = revenue;
        this.count = count;
    }

    // TYPE / TOP (3 field quan trọng)
    public IncidentDashboardResponse(IncidentEnum type, BigDecimal revenue, Long count) {
        this.type = type;
        this.revenue = revenue;
        this.count = count;
    }

    public IncidentDashboardResponse(SourceEnum sourceEnum, Long count) {
        this.sourceEnum = sourceEnum;
        this.count = count;
    }

    public IncidentDashboardResponse(IncidentEnum type, Long count) {
        this.type = type;
        this.count = count;
    }

    public IncidentDashboardResponse(IncidentEnum type,  BigDecimal revenue) {
        this.type = type;
        this.revenue = revenue;
    }
}