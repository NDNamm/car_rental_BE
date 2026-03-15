package com.example.car_rental.controller;

import com.example.car_rental.dto.requestDTO.CarRequest.CarFeatureRequest;
import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.CategoryResponse;
import com.example.car_rental.service.FeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feature")
public class FeatureController {

    private final FeatureService featureService;

    @GetMapping("")
    public ApiResponse<List<CategoryResponse>> getAllFeature() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .code(200)
                .data(featureService.getAllFeature())
                .build();
    }

    @PostMapping(value = "/add")
    public ApiResponse<Void> addFeature(@RequestBody CarFeatureRequest request) {

        featureService.addFeature(request);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Create Feature successful")
                .build();
    }


    @PutMapping(value = "/update/{featureId}")
    public ApiResponse<Void> addFeature(@PathVariable(value = "featureId") Long featureId,
                                        @RequestBody CarFeatureRequest request) {

        featureService.updateFeature(featureId, request);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Update Feature successful")
                .build();
    }

    @DeleteMapping("/delete/{featureId}")
    public ApiResponse<Void> deleteFeature(@PathVariable("featureId") Long featureId) {

        featureService.deleteFeature(featureId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete Feature successful")
                .build();
    }

}
