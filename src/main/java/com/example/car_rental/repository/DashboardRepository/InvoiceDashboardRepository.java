package com.example.car_rental.repository.DashboardRepository;

import com.example.car_rental.dto.responseDTO.DashboardResponse.Booking.BookingChartResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.Incident.IncidentChartResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.Invoice.InvoiceChartResponse;
import com.example.car_rental.entity.Invoices;
import com.example.car_rental.entity.enums.BookingStatusEnum;
import com.example.car_rental.entity.enums.InvoiceStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface InvoiceDashboardRepository extends JpaRepository<Invoices, Long> {

        Long countBy();

        Long countByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

        Long countByStatus(InvoiceStatusEnum status);

        @Query("""
                            SELECT COALESCE(SUM(i.finalAmount),0)
                            FROM Invoices i
                            WHERE i.status = 'PAID'
                        """)
        BigDecimal getTotalAllRevenue();

        @Query("""
                            SELECT COALESCE(SUM(i.finalAmount),0)
                              FROM Invoices i
                              WHERE i.status = 'PAID'
                              AND i.createdAt BETWEEN :startDate AND :endDate
                        """)
        BigDecimal getTotalRevenue(
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        Long countByStatusAndCreatedAtBetween(
                        InvoiceStatusEnum status,
                        LocalDateTime from,
                        LocalDateTime to);

        // ================= DAILY =================
        @Query("""
                            SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.Invoice.InvoiceChartResponse(
                                FUNCTION('DATE', i.createdAt),
                                COUNT(i),
                                COALESCE(SUM(i.finalAmount),0)
                            )
                            FROM Invoices i
                            WHERE i.status = 'PAID'
                              AND i.createdAt BETWEEN :startDate AND :endDate
                            GROUP BY FUNCTION('DATE', i.createdAt)
                            ORDER BY FUNCTION('DATE', i.createdAt)
                        """)
        List<InvoiceChartResponse> getDailyChart(
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        // ================= MONTHLY =================
        @Query("""
                            SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.Invoice.InvoiceChartResponse(
                                MONTH(i.createdAt),
                                COUNT(i),
                                COALESCE(SUM(i.finalAmount),0)
                            )
                            FROM Invoices i
                            WHERE i.status = 'PAID'
                              AND i.createdAt BETWEEN :startDate AND :endDate
                            GROUP BY MONTH(i.createdAt), YEAR(i.createdAt)
                            ORDER BY YEAR(i.createdAt), MONTH(i.createdAt)
                        """)
        List<InvoiceChartResponse> getMonthlyChart(
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        // ================= YEARLY =================
        @Query("""
                            SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.Invoice.InvoiceChartResponse(
                                YEAR(i.createdAt),
                                COUNT(i),
                                COALESCE(SUM(i.finalAmount),0)
                            )
                            FROM Invoices i
                            WHERE i.status = 'PAID'
                              AND i.createdAt BETWEEN :startDate AND :endDate
                            GROUP BY YEAR(i.createdAt)
                            ORDER BY YEAR(i.createdAt)
                        """)
        List<InvoiceChartResponse> getYearlyChart(
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        @Query("""
                            SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.Invoice.InvoiceChartResponse(
                                HOUR(i.createdAt),
                                COUNT(i),
                                COALESCE(SUM(i.finalAmount),0)
                            )
                            FROM Invoices i
                            WHERE i.status = 'PAID'
                              AND i.createdAt BETWEEN :startDate AND :endDate
                            GROUP BY HOUR(i.createdAt)
                            ORDER BY HOUR(i.createdAt)
                        """)
        List<InvoiceChartResponse> getHourlyChart(
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);
}