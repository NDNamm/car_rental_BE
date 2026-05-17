package com.example.car_rental.service;

import com.example.car_rental.dto.requestDTO.PenaltyRequest.PenaltyRequest;
import com.example.car_rental.entity.enums.SourceEnum;

import java.math.BigDecimal;

public interface PenaltyService {
    void createPenalty(Long incidentId, BigDecimal amount);
}