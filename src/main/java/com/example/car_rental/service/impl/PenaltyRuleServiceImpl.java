package com.example.car_rental.service.impl;

import com.example.car_rental.dto.responseDTO.PenaltyResponse.PenaltyRuleResponse;
import com.example.car_rental.entity.PenaltyRules;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.PenaltyRuleRepository;
import com.example.car_rental.service.PenaltyRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PenaltyRuleServiceImpl implements PenaltyRuleService {

    final private PenaltyRuleRepository penaltyRuleRepository;


    @Override
    public Page<PenaltyRuleResponse> getAllPenaltyRules(String type, Boolean isActive, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<PenaltyRules> penaltyRulePage = null;
        boolean noFilter =
                (type == null || type.isBlank()) &&
                        (isActive == null);

        if (noFilter) {
            penaltyRulePage = penaltyRuleRepository.search(null,null, pageable);
        } else {
            penaltyRulePage = penaltyRuleRepository.search(type, isActive, pageable);
        }

        return penaltyRulePage.map(penaltyRules ->
                PenaltyRuleResponse.builder()
                        .id(penaltyRules.getId())
                        .description(penaltyRules.getDescription())
                        .unitPrice(penaltyRules.getUnitPrice())
                        .type(penaltyRules.getType())
                        .isActive(penaltyRules.getIsActive())
                        .createdAt(penaltyRules.getCreatedAt())
                        .build()
        );
    }

    @Override
    public void createPenaltyRule(PenaltyRuleResponse request) {
        PenaltyRules penaltyRules = PenaltyRules.builder()
                .description(request.getDescription())
                .unitPrice(request.getUnitPrice())
                .type(request.getType().trim().toUpperCase())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();
        penaltyRuleRepository.save(penaltyRules);
    }

    @Override
    public void updatePenaltyRule(Long id, PenaltyRuleResponse request) {
        PenaltyRules penaltyRules = penaltyRuleRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PENALTY_RULES_NOT_FOUND));

        if (request.getDescription() != null) {
            penaltyRules.setDescription(request.getDescription());
        }
        if (request.getUnitPrice() != null) {
            penaltyRules.setUnitPrice(request.getUnitPrice());
        }
        if (request.getType() != null) {
            penaltyRules.setType(request.getType().trim().toUpperCase());
        }
        if (request.getIsActive() != null) {
            penaltyRules.setIsActive(request.getIsActive());
        }
        penaltyRuleRepository.save(penaltyRules);
    }

    @Override
    public void deletePenaltyRule(Long id) {
        PenaltyRules penaltyRules = penaltyRuleRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PENALTY_RULES_NOT_FOUND));

        penaltyRuleRepository.delete(penaltyRules);
    }
}
