package com.example.car_rental.service;

import com.example.car_rental.dto.requestDTO.PenaltyRequest.PenaltyRequest;

public interface PenaltyService {
    public void createAutoPenalty(Long incidentId, PenaltyRequest request);
    public void createManualPenalty(Long incidentId, PenaltyRequest request);
}