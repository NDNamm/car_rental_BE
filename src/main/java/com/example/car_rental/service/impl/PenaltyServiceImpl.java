package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.PenaltyRequest.PenaltyRequest;
import com.example.car_rental.entity.Incidents;
import com.example.car_rental.entity.Penalties;
import com.example.car_rental.entity.enums.PenaltyStatusEnum;
import com.example.car_rental.entity.enums.SourceEnum;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.IncidentRepository;
import com.example.car_rental.repository.PenaltyRepository;
import com.example.car_rental.service.PenaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PenaltyServiceImpl implements PenaltyService {

    private final IncidentRepository incidentRepository;

    private final PenaltyRepository penaltyRepository;

    @Override
    public void createPenalty(Long incidentId, BigDecimal amount) {
        Incidents incidents = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new AppException(ErrorCode.INCIDENT_NOT_FOUND));

        Penalties penalties = Penalties.builder()
                        .amount(amount)
                        .incident(incidents)
                        .createdAt(LocalDateTime.now())
                        .status(PenaltyStatusEnum.UNPAID)
                        .build();

        penaltyRepository.save(penalties);
    }
}