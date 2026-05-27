package com.example.car_rental.service.DashboardService.impl;

import com.example.car_rental.dto.responseDTO.DashboardResponse.IncidentDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.InvoiceDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse;
import com.example.car_rental.repository.DashboardRepository.IncidentDashboardRepository;
import com.example.car_rental.service.DashboardService.IncidentDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentDashboardServiceImpl implements IncidentDashboardService {

    private final IncidentDashboardRepository incidentDashboardRepository;

    // ================= INCIDENT =================

    @Override
    public Long getTotalIncidents() {
        return incidentDashboardRepository.getTotalIncidents();
    }

    @Override
    public List<IncidentDashboardResponse> getIncidentByType() {
        return incidentDashboardRepository.getIncidentByType();
    }

    @Override
    public List<IncidentDashboardResponse> getIncidentBySource() {
        return incidentDashboardRepository.getIncidentBySource();
    }

    @Override
    public List<IncidentDashboardResponse> getIncidentByDay(LocalDateTime from, LocalDateTime to) {
        return incidentDashboardRepository.getIncidentByDay(from, to);
    }

    @Override
    public List<IncidentDashboardResponse> getIncidentByMonth(int year) {
        return incidentDashboardRepository.getIncidentByMonth(year);
    }

    @Override
    public List<IncidentDashboardResponse> getInvoiceByYear() {
        return incidentDashboardRepository.getIncidentByYear();
    }

    // ================= DAMAGE =================

    @Override
    public BigDecimal getTotalDamageCost() {
        return incidentDashboardRepository.getTotalDamageCost();
    }

    @Override
    public List<IncidentDashboardResponse> getDamageByIncidentType() {
        return incidentDashboardRepository.getDamageByIncidentType();
    }

    @Override
    public List<IncidentDashboardResponse> getTopExpensiveIncidents() {
        return incidentDashboardRepository.getTopExpensiveIncidents();
    }
}