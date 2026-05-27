package com.example.car_rental.service.DashboardService;

import com.example.car_rental.dto.responseDTO.DashboardResponse.IncidentDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.InvoiceDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface IncidentDashboardService {

    Long getTotalIncidents();
    List<IncidentDashboardResponse> getIncidentByType();
    List<IncidentDashboardResponse> getIncidentBySource();
    List<IncidentDashboardResponse> getIncidentByDay(LocalDateTime from, LocalDateTime to);
    List<IncidentDashboardResponse> getIncidentByMonth(int year);
    List<IncidentDashboardResponse> getInvoiceByYear();
    // DAMAGE (PENALTY)
    BigDecimal getTotalDamageCost();
    List<IncidentDashboardResponse> getDamageByIncidentType();
    List<IncidentDashboardResponse> getTopExpensiveIncidents();
}