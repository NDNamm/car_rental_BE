package com.example.car_rental.repository.DashboardRepository;

import com.example.car_rental.dto.responseDTO.DashboardResponse.IncidentDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.InvoiceDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse;
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

    @Query("SELECT COUNT(i) FROM Incidents i")
    Long getTotalIncidents();

    @Query("""
                SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.IncidentDashboardResponse(
                    i.type,
                    COUNT(i)
                )
                FROM Incidents i
                GROUP BY i.type
            """)
    List<IncidentDashboardResponse> getIncidentByType();

    @Query("""
                SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.IncidentDashboardResponse(
                    i.source,
                    COUNT(i)
                )
                FROM Incidents i
                GROUP BY i.source
            """)
    List<IncidentDashboardResponse> getIncidentBySource();

    @Query("""
                SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.IncidentDashboardResponse(
                    CAST(i.createdAt AS date),
                    SUM(p.amount),
                    COUNT(DISTINCT i.id)
                )
                FROM Incidents i
                JOIN i.penalties p
                WHERE i.createdAt BETWEEN :start AND :end
                GROUP BY CAST(i.createdAt AS date)
                ORDER BY CAST(i.createdAt AS date)
            """)
    List<IncidentDashboardResponse> getIncidentByDay(LocalDateTime start, LocalDateTime end);

    @Query("""
                SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.IncidentDashboardResponse(
                      MONTH(i.createdAt),
                      SUM(p.amount),
                      COUNT(DISTINCT i.id)
                )
                FROM Incidents i
                JOIN i.penalties p
                WHERE YEAR(i.createdAt) = :year
                GROUP BY MONTH(i.createdAt)
                ORDER BY MONTH(i.createdAt)
            """)
    List<IncidentDashboardResponse> getIncidentByMonth(@Param("year") int year);

    @Query("""
                SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.IncidentDashboardResponse(
                    YEAR(i.createdAt),
                    SUM(p.amount),
                    COUNT(DISTINCT i.id)
                )
                FROM Incidents i
                JOIN i.penalties p
                GROUP BY YEAR(i.createdAt)
                ORDER BY YEAR(i.createdAt)
            """)
    List<IncidentDashboardResponse> getIncidentByYear();

    // =========================
    // PENALTY (JOIN INCIDENT)
    // =========================

    @Query("""
                SELECT COALESCE(SUM(p.amount), 0)
                FROM Incidents i
                JOIN i.penalties p
            """)
    BigDecimal getTotalDamageCost();

    @Query("""
                SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.IncidentDashboardResponse(
                    i.type,
                    SUM(p.amount),
                    COUNT(i)
                )
                FROM Incidents i
                JOIN i.penalties p
                GROUP BY i.type
                ORDER BY COUNT(i) DESC
            """)
    List<IncidentDashboardResponse> getDamageByIncidentType();

    @Query("""
                SELECT new com.example.car_rental.dto.responseDTO.DashboardResponse.IncidentDashboardResponse(
                    i.type,
                    SUM(p.amount)
                )
                FROM Incidents i
                JOIN i.penalties p
                GROUP BY i.id
                ORDER BY SUM(p.amount) DESC
            """)
    List<IncidentDashboardResponse> getTopExpensiveIncidents();
}