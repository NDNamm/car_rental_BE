package com.example.car_rental.service;

import com.example.car_rental.dto.requestDTO.CarRequest.FeatureRequest;
import com.example.car_rental.dto.responseDTO.CategoryResponse;

import java.util.List;

public interface FeatureService {
    void addFeature(FeatureRequest request);
    void updateFeature(Long featureId, FeatureRequest request);
    void deleteFeature(Long featureId);
    List<CategoryResponse> getAllFeature();
}
