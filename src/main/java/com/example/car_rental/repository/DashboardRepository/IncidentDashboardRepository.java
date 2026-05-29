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

    @Query("""
                SELECT COALESCE(SUM(p.amount),0)
                FROM Incidents i
                JOIN i.penalties p
            """)
    BigDecimal getTotalDamageCost();

    // CHART
    @Query("""
                SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.Incident.IncidentChartResponse(
                    FUNCTION('DATE', i.createdAt),
                    COUNT(i),
                    COALESCE(SUM(p.amount),0)
                )
                FROM Incidents i
                LEFT JOIN i.penalties p
                WHERE i.createdAt >= :startDate
                GROUP BY FUNCTION('DATE', i.createdAt)
                ORDER BY FUNCTION('DATE', i.createdAt)
            """)
    List<IncidentChartResponse> getDailyChart(@Param("startDate") LocalDateTime startDate);

    @Query("""
                SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.Incident.IncidentChartResponse(
                    MONTH(i.createdAt),
                    COUNT(i),
                    COALESCE(SUM(p.amount),0)
                )
                FROM Incidents i
                LEFT JOIN i.penalties p
                GROUP BY MONTH(i.createdAt)
                ORDER BY MONTH(i.createdAt)
            """)
    List<IncidentChartResponse> getMonthlyChart();


    @Query("""
                SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.Incident.IncidentChartResponse(
                    YEAR(i.createdAt),
                    COUNT(i),
                    COALESCE(SUM(p.amount),0)
                )
                FROM Incidents i
                LEFT JOIN i.penalties p
                GROUP BY YEAR(i.createdAt)
                ORDER BY YEAR(i.createdAt)
            """)
    List<IncidentChartResponse> getYearlyChart();

    // TOP INCIDENT (FREQUENCY + COST)
    @Query("""
                SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.Incident.IncidentTopResponse(
                    i.source,
                    i.type,
                    COUNT(i),
                    COALESCE(SUM(p.amount),0)
                )
                FROM Incidents i
                LEFT JOIN i.penalties p
                GROUP BY i.source, i.type
                ORDER BY COUNT(i) DESC
            """)
    List<IncidentTopResponse> getTopIncidentsByFrequency();
}