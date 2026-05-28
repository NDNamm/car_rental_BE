package com.example.car_rental.dto.responseDTO.DashboardResponse.Invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceStatisticsResponse {

    private Long totalInvoices;
    private Long paidInvoices;
    private Long cancelInvoices;
    private Long pendingInvoices;
    private Long partialInvoices;
    private BigDecimal totalRevenue;
}
