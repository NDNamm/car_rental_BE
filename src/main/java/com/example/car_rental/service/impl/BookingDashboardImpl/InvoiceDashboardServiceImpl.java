package com.example.car_rental.service.impl.BookingDashboardImpl;

import com.example.car_rental.dto.responseDTO.DashboardResponse.InvoiceDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse;
import com.example.car_rental.repository.DashboardRepository.InvoiceDashboardRepository;
import com.example.car_rental.service.DashboardService.InvoiceDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceDashboardServiceImpl implements InvoiceDashboardService {

    private final InvoiceDashboardRepository invoiceDashboardRepository;

    @Override
    public List<InvoiceDashboardResponse> getInvoiceByDay(LocalDateTime from, LocalDateTime to) {
        return invoiceDashboardRepository.getRevenueByDay(from, to);
    }

    @Override
    public List<InvoiceDashboardResponse> getInvoiceByMonth(int year) {
        return invoiceDashboardRepository.getRevenueByMonth(year);
    }

    @Override
    public List<InvoiceDashboardResponse> getInvoiceByYear() {
        return invoiceDashboardRepository.getRevenueByYear();
    }

    @Override
    public List<StatusCountResponse> getInvoiceStatusCount() {
        return invoiceDashboardRepository.getInvoiceStatusCount();
    }

    @Override
    public BigDecimal getTotalRevenue() {
        return invoiceDashboardRepository.getTotalRevenue();
    }

    @Override
    public Long getTotalInvoices() {
        return invoiceDashboardRepository.getTotalInvoices();
    }
}