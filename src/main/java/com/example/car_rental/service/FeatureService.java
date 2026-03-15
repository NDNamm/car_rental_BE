package com.example.car_rental.service;

import com.example.car_rental.dto.requestDTO.CarRequest.CarFeatureRequest;
import com.example.car_rental.dto.responseDTO.CategoryResponse;

import java.util.List;

public interface FeatureService {
    void addFeature(CarFeatureRequest request);
    void updateFeature(Long featureId, CarFeatureRequest request);
    void deleteFeature(Long featureId);
    List<CategoryResponse> getAllFeature();
}
