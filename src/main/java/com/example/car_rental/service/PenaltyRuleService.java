package com.example.car_rental.service;

import com.example.car_rental.dto.responseDTO.PenaltyResponse.PenaltyRuleResponse;
import org.springframework.data.domain.Page;


public interface PenaltyRuleService {
    Page<PenaltyRuleResponse> getAllPenaltyRules(String type, Boolean isActive, int page, int size);

    void createPenaltyRule(PenaltyRuleResponse request);

    void updatePenaltyRule(Long id, PenaltyRuleResponse request);

    void deletePenaltyRule(Long id);
}