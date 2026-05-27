package com.example.car_rental.service.DashboardService;

import com.example.car_rental.dto.responseDTO.DashboardResponse.InvoiceDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface InvoiceDashboardService {
    List<InvoiceDashboardResponse> getInvoiceByDay(LocalDateTime from, LocalDateTime to);
    List<InvoiceDashboardResponse> getInvoiceByMonth(int year);
    List<InvoiceDashboardResponse> getInvoiceByYear();
    List<StatusCountResponse> getInvoiceStatusCount();
    BigDecimal getTotalRevenue();
    Long getTotalInvoices();
}
