package com.example.car_rental.repository.DashboardRepository;

import com.example.car_rental.dto.responseDTO.DashboardResponse.Booking.BookingChartResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.Invoice.InvoiceChartResponse;
import com.example.car_rental.entity.Invoices;
import com.example.car_rental.entity.enums.InvoiceStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceDashboardRepository extends JpaRepository<Invoices, Long> {

    Long countBy();

    Long countByStatus(InvoiceStatusEnum status);

    @Query("""
        SELECT COALESCE(SUM(i.finalAmount),0)
        FROM Invoices i
        WHERE i.status='PAID'
    """)
    BigDecimal getTotalRevenue();

    @Query("""
        SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.Invoice.InvoiceChartResponse(
            FUNCTION('DATE', i.createdAt),
            COUNT(i),
            COALESCE(SUM(i.finalAmount),0)
        )
        FROM Invoices i
        WHERE i.status='PAID'
        GROUP BY  FUNCTION('DATE', i.createdAt)
    """)
    List<InvoiceChartResponse> getDailyChart();

}