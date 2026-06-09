package com.example.car_rental.repository.DashboardRepository;

import com.example.car_rental.dto.responseDTO.DashboardResponse.Booking.BookingChartResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.Incident.IncidentChartResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.Incident.IncidentTopResponse;
import com.example.car_rental.entity.Incidents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IncidentDashboardRepository extends JpaRepository<Incidents, Long> {

    Long countBy();

    Long countByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

    @Query("""
                SELECT COALESCE(SUM(p.amount),0)
                FROM Incidents i
                JOIN i.penalties p
            """)
    BigDecimal getTotalAllDamageCost();

    @Query("""
                  SELECT COALESCE(SUM(p.amount),0)
                  FROM Incidents i
                  JOIN i.penalties p
                  WHERE i.createdAt BETWEEN :startDate AND :endDate
            """)
    BigDecimal getTotalDamageCost(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("""
                SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.Incident.IncidentChartResponse(
                    HOUR(i.createdAt),
                    COUNT(i),
                    COALESCE(SUM(p.amount),0)
                )
                FROM Incidents i
                LEFT JOIN i.penalties p
                WHERE i.createdAt BETWEEN :startDate AND :endDate
                GROUP BY HOUR(i.createdAt)
                ORDER BY HOUR(i.createdAt)
            """)
    List<IncidentChartResponse> getHourlyChart(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // ================= DAILY =================
    @Query("""
                SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.Incident.IncidentChartResponse(
                    FUNCTION('DATE', i.createdAt),
                    COUNT(i),
                    COALESCE(SUM(p.amount),0)
                )
                FROM Incidents i
                LEFT JOIN i.penalties p
                WHERE i.createdAt BETWEEN :startDate AND :endDate
                GROUP BY FUNCTION('DATE', i.createdAt)
                ORDER BY FUNCTION('DATE', i.createdAt)
            """)
    List<IncidentChartResponse> getDailyChart(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // ================= MONTHLY =================
    @Query("""
                SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.Incident.IncidentChartResponse(
                    MONTH(i.createdAt),
                    COUNT(i),
                    COALESCE(SUM(p.amount),0)
                )
                FROM Incidents i
                LEFT JOIN i.penalties p
                WHERE i.createdAt BETWEEN :startDate AND :endDate
                GROUP BY MONTH(i.createdAt), YEAR(i.createdAt)
                ORDER BY YEAR(i.createdAt), MONTH(i.createdAt)
            """)
    List<IncidentChartResponse> getMonthlyChart(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // ================= YEARLY =================
    @Query("""
                SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.Incident.IncidentChartResponse(
                    YEAR(i.createdAt),
                    COUNT(i),
                    COALESCE(SUM(p.amount),0)
                )
                FROM Incidents i
                LEFT JOIN i.penalties p
                WHERE i.createdAt BETWEEN :startDate AND :endDate
                GROUP BY YEAR(i.createdAt)
                ORDER BY YEAR(i.createdAt)
            """)
    List<IncidentChartResponse> getYearlyChart(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // ================= TOP =================
    @Query("""
                SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.Incident.IncidentTopResponse(
                    i.source,
                    i.type,
                    COUNT(i),
                    COALESCE(SUM(p.amount),0)
                )
                FROM Incidents i
                LEFT JOIN i.penalties p
                WHERE i.createdAt BETWEEN :from AND :to
                GROUP BY i.source, i.type
                ORDER BY COUNT(i) DESC
            """)
    List<IncidentTopResponse> getTopIncidentsByFrequency(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

}