package com.example.car_rental.dto.responseDTO.DashboardResponse;

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
public class InvoiceDashboardResponse {
    private LocalDate  date;
    private int month;
    private BigDecimal revenue;
    private Long count;
    private Object label;

    public InvoiceDashboardResponse(LocalDateTime date, BigDecimal revenue, Long count) {
        this.date = date.toLocalDate();
        this.revenue = revenue;
        this.count = count;
    }

    public InvoiceDashboardResponse(Object label, BigDecimal revenue, Long count) {
        this.label = label;
        this.revenue = revenue;
        this.count = count;
    }
}
