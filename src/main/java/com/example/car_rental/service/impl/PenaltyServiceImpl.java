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

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PenaltyServiceImpl implements PenaltyService {

    final private IncidentRepository incidentRepository;
    final private PenaltyRepository penaltyRepository;

    @Override
    public void createAutoPenalty(Long incidentId, PenaltyRequest request) {

        Incidents incidents = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new AppException(ErrorCode.INCIDENT_NOT_FOUND));

        Penalties penalties = Penalties.builder()
                .reason(request.getReason())
                .amount(request.getAmount())
                .incident(incidents)
                .createdAt(LocalDateTime.now())
                .source(SourceEnum.AUTO)
                .status(PenaltyStatusEnum.UNPAID)
                .build();

        penaltyRepository.save(penalties);
    }

    @Override
    public void createManualPenalty(Long incidentId, PenaltyRequest request) {
        Incidents incidents = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new AppException(ErrorCode.INCIDENT_NOT_FOUND));

        Penalties penalties = Penalties.builder()
                .reason(request.getReason())
                .amount(request.getAmount())
                .incident(incidents)
                .createdAt(LocalDateTime.now())
                .source(SourceEnum.MANUAL)
                .status(PenaltyStatusEnum.UNPAID)
                .build();

        penaltyRepository.save(penalties);
    }
}
