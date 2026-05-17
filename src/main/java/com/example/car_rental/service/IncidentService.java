package com.example.car_rental.service;

import com.example.car_rental.dto.requestDTO.IncidentRequest;
import com.example.car_rental.entity.Incidents;
import com.example.car_rental.entity.Users;
import com.example.car_rental.entity.enums.IncidentEnum;

public interface IncidentService {
    Incidents createAutoIncident(Long bookingDetailId, Users staff, IncidentEnum type, String description);
    void handleLateReturn(Long bookingDetailId);
//    void handleExceededMileage(Long bookingDetailId, Long currentMileage);
//    void handleFuelLoss(Long bookingDetailId, Integer currentFuel);

    void createManualIncident(Long bookingDetailId, IncidentRequest request, Users staff);
}