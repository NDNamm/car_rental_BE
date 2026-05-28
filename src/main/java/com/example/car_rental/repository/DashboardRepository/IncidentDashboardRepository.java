package com.example.car_rental.repository.DashboardRepository;

import com.example.car_rental.dto.responseDTO.DashboardResponse.Incident.IncidentChartResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.Incident.IncidentTopResponse;
import com.example.car_rental.entity.Incidents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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
                GROUP BY FUNCTION('DATE', i.createdAt)
            """)
    List<IncidentChartResponse> getDailyChart();

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