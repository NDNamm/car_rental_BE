package com.example.car_rental.service;

import com.example.car_rental.dto.requestDTO.IncidentRequest;
import com.example.car_rental.entity.Incidents;
import com.example.car_rental.entity.Users;
import com.example.car_rental.entity.enums.IncidentEnum;

import java.util.List;

public interface IncidentService {
    Incidents createAutoIncident(Long handoverId, Users staff, IncidentEnum type, String description);
    void handleLateReturn(Long handoverId);
//    void handleExceededMileage(Long bookingDetailId, Long currentMileage);
//    void handleFuelLoss(Long bookingDetailId, Integer currentFuel);

    void createManualIncident(Long handoverId, List<IncidentRequest> requests, Users staff);
}