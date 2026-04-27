package com.example.car_rental.service;

import com.example.car_rental.dto.requestDTO.CarRequest.FeatureRequest;
import com.example.car_rental.dto.responseDTO.CarResponse.FeatureResponse;
import com.example.car_rental.dto.responseDTO.CategoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FeatureService {
    void addFeature(FeatureRequest request);
    void updateFeature(Long featureId, FeatureRequest request);
    void deleteFeature(Long featureId);
    Page<FeatureResponse> getAllFeature(int page, int size, String name);
}
