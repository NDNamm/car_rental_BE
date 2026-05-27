package com.example.car_rental.controller.DashboardController;

import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.IncidentDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.InvoiceDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse;
import com.example.car_rental.service.DashboardService.IncidentDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard/incidents")
public class IncidentDashboardController {

    private final IncidentDashboardService incidentDashboardService;

    // ================= INCIDENT =================

    @GetMapping("/total")
    public ApiResponse<Long> getTotalIncidents() {
        ApiResponse<Long> response = new ApiResponse<>();
        response.setData(incidentDashboardService.getTotalIncidents());
        return response;
    }

    @GetMapping("/type")
    public ApiResponse<List<IncidentDashboardResponse>> getIncidentByType() {
        ApiResponse<List<IncidentDashboardResponse>> response = new ApiResponse<>();
        response.setData(incidentDashboardService.getIncidentByType());
        return response;
    }

    @GetMapping("/source")
    public ApiResponse<List<IncidentDashboardResponse>> getIncidentBySource() {
        ApiResponse<List<IncidentDashboardResponse>> response = new ApiResponse<>();
        response.setData(incidentDashboardService.getIncidentBySource());
        return response;
    }

    @GetMapping("/daily")
    public ApiResponse<List<IncidentDashboardResponse>> getIncidentByDay() {

        LocalDateTime start = LocalDate.now().minusDays(7).atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(23, 59, 59);

        ApiResponse<List<IncidentDashboardResponse>> response = new ApiResponse<>();
        response.setData(incidentDashboardService.getIncidentByDay(start, end));
        return response;
    }

    @GetMapping("/month")
    public ApiResponse<List<IncidentDashboardResponse>> getInvoiceByMonth(@RequestParam int year) {

        ApiResponse<List<IncidentDashboardResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(incidentDashboardService.getIncidentByMonth(year));
        return apiResponse;
    }

    @GetMapping("/year")
    public ApiResponse<List<IncidentDashboardResponse>> getIncidentByYear() {

        ApiResponse<List<IncidentDashboardResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(incidentDashboardService.getInvoiceByYear());
        return apiResponse;
    }


    // ================= DAMAGE (PENALTY) =================

    @GetMapping("/damage/total")
    public ApiResponse<BigDecimal> getTotalDamage() {
        ApiResponse<BigDecimal> response = new ApiResponse<>();
        response.setData(incidentDashboardService.getTotalDamageCost());
        return response;
    }

    @GetMapping("/damage/by-type")
    public ApiResponse<List<IncidentDashboardResponse>> getDamageByType() {
        ApiResponse<List<IncidentDashboardResponse>> response = new ApiResponse<>();
        response.setData(incidentDashboardService.getDamageByIncidentType());
        return response;
    }

    @GetMapping("/damage/top")
    public ApiResponse<List<IncidentDashboardResponse>> getTopExpensiveIncidents() {
        ApiResponse<List<IncidentDashboardResponse>> response = new ApiResponse<>();
        response.setData(incidentDashboardService.getTopExpensiveIncidents());
        return response;
    }
}