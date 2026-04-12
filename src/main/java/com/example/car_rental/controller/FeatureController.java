package com.example.car_rental.controller;

import com.example.car_rental.dto.requestDTO.CarRequest.FeatureRequest;
import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.CategoryResponse;
import com.example.car_rental.service.FeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/features")
public class FeatureController {

    private final FeatureService featureService;

    @GetMapping("")
    public ApiResponse<List<CategoryResponse>> getAllFeature() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .code(200)
                .data(featureService.getAllFeature())
                .build();
    }

    @PostMapping(value = "")
    public ApiResponse<Void> addFeature(@RequestBody FeatureRequest request) {

        featureService.addFeature(request);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Create Feature successful")
                .build();
    }


    @PutMapping(value = "/{featureId}")
    public ApiResponse<Void> addFeature(@PathVariable(value = "featureId") Long featureId,
                                        @RequestBody FeatureRequest request) {

        featureService.updateFeature(featureId, request);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Update Feature successful")
                .build();
    }

    @DeleteMapping("/{featureId}")
    public ApiResponse<Void> deleteFeature(@PathVariable("featureId") Long featureId) {

        featureService.deleteFeature(featureId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete Feature successful")
                .build();
    }

}
