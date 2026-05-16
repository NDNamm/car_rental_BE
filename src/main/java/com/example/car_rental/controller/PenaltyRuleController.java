package com.example.car_rental.controller;

import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.PenaltyResponse.PenaltyRuleResponse;
import com.example.car_rental.service.PenaltyRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/penalty-rules")
@RequiredArgsConstructor
public class PenaltyRuleController {

    final private PenaltyRuleService penaltyRuleService;

    @GetMapping("")
    public ApiResponse<Page<PenaltyRuleResponse>> getAllPenaltyRules(@RequestParam(defaultValue = "0", required = false) int page,
                                                                     @RequestParam(defaultValue = "5") int size,
                                                                     @RequestParam(required = false) String type,
                                                                     @RequestParam(required = false) Boolean isActive) {
        return ApiResponse.<Page<PenaltyRuleResponse>>builder()
                .code(200)
                .data(penaltyRuleService.getAllPenaltyRules(type, isActive, page, size))
                .build();
    }

    @PostMapping(value = "")
    public ApiResponse<Void> createPenaltyRules(@RequestBody PenaltyRuleResponse request){

        penaltyRuleService.createPenaltyRule(request);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Create penalty rule successful")
                .build();
    }


    @PutMapping(value = "/{penaltyRuleId}")
    public ApiResponse<Void> updatePenaltyRule(@PathVariable(value = "penaltyRuleId") Long penaltyRuleId,
                                            @RequestBody PenaltyRuleResponse request){

        penaltyRuleService.updatePenaltyRule(penaltyRuleId, request);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Update penalty rule successful")
                .build();
    }

    @DeleteMapping("/{penaltyRuleId}")
    public ApiResponse<Void> deletePenaltyRule(@PathVariable("penaltyRuleId") Long penaltyRuleId) {

        penaltyRuleService.deletePenaltyRule(penaltyRuleId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete penalty rule successful")
                .build();
    }

}
