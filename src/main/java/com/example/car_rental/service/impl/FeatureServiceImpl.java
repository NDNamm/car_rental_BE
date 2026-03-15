package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.CarRequest.CarFeatureRequest;
import com.example.car_rental.dto.responseDTO.CategoryResponse;
import com.example.car_rental.entity.CarFeatures;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.FeatureRepository;
import com.example.car_rental.service.FeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements FeatureService {

    private final FeatureRepository featureRepository;

    @Override
    public void addFeature(CarFeatureRequest request) {
        if (featureRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.FEATURE_EXISTED);
        }

        CarFeatures carFeatures = CarFeatures.builder()
                .name(request.getName())
                .build();

        featureRepository.save(carFeatures);
    }

    @Override
    public void updateFeature(Long featureId, CarFeatureRequest request) {
        CarFeatures carFeatures = featureRepository.findById(featureId)
                .orElseThrow(() -> new AppException(ErrorCode.FEATURE_NOT_FOUND));

        if (request.getName() != null && !request.getName().isEmpty()) {
            if (featureRepository.existsByName(request.getName())) {
                throw new AppException(ErrorCode.FEATURE_EXISTED);
            }
            carFeatures.setName(request.getName());
        }

        featureRepository.save(carFeatures);
    }

    @Override
    public void deleteFeature(Long featureId) {
        CarFeatures carFeatures = featureRepository.findById(featureId)
                .orElseThrow(() -> new AppException(ErrorCode.FEATURE_NOT_FOUND));
        featureRepository.delete(carFeatures);
    }

    @Override
    public List<CategoryResponse> getAllFeature() {

        List<CarFeatures> carFeatures = featureRepository.findAll();

        return carFeatures.stream().map(categories -> CategoryResponse.builder()
                .id(categories.getId())
                .name(categories.getName())
                .build())
                .toList();

    }

}
