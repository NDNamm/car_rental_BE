package com.example.car_rental.repository.DashboardRepository;

import com.example.car_rental.dto.responseDTO.DashboardResponse.BookingDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.InvoiceDashboardResponse;
import com.example.car_rental.entity.Invoices;
import com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InvoiceDashboardRepository extends JpaRepository<Invoices, Long> {


    @Query("""
            SELECT COALESCE(SUM(i.finalAmount), 0)
            FROM Invoices i
            WHERE i.status = 'PAID'
            """)
    BigDecimal getTotalRevenue();

    @Query("""
            SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.InvoiceDashboardResponse(
                MIN(i.createdAt),
                SUM(i.finalAmount),
                COUNT(i.id)
            )
            FROM Invoices i
            WHERE i.createdAt BETWEEN :start AND :end
            AND i.status = 'PAID'
            GROUP BY FUNCTION('DATE', i.createdAt)
            ORDER BY FUNCTION('DATE', i.createdAt)
            """)
    List<InvoiceDashboardResponse> getRevenueByDay(@Param("start") LocalDateTime start,
                    @Param("end") LocalDateTime end);

    @Query("""
            SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.InvoiceDashboardResponse(
                MONTH(i.createdAt),
                SUM(i.finalAmount),
                COUNT(i.id)
            )
            FROM Invoices i
            WHERE YEAR(i.createdAt) = :year
            AND i.status = 'PAID'
            GROUP BY MONTH(i.createdAt)
            ORDER BY MONTH(i.createdAt)
            """)
    List<InvoiceDashboardResponse> getRevenueByMonth(@Param("year") int year);

    @Query("""
        SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.InvoiceDashboardResponse(
            YEAR(i.createdAt),
            SUM(i.finalAmount),
            COUNT(i.id)
        )
        FROM Invoices i
        WHERE i.status = 'PAID'
        GROUP BY YEAR(i.createdAt)
        ORDER BY YEAR(i.createdAt)
        """)
    List<InvoiceDashboardResponse> getRevenueByYear();

    @Query("""
            SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse(
                i.status,
                COUNT(i)
            )
            FROM Invoices i
            GROUP BY i.status
            """)
    List<StatusCountResponse> getInvoiceStatusCount();

    @Query("SELECT COUNT(i) FROM Invoices i")
    Long getTotalInvoices();

}