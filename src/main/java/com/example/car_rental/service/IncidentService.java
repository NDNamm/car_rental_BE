package com.example.car_rental.service;

import com.example.car_rental.dto.requestDTO.IncidentRequest;
import com.example.car_rental.entity.BookingDetails;
import com.example.car_rental.entity.Incidents;
import com.example.car_rental.entity.Users;
import com.example.car_rental.entity.enums.IncidentEnum;

public interface IncidentService {
    public void createAutoIncident(Long bookingDetailId , Users staff, IncidentRequest request);
}