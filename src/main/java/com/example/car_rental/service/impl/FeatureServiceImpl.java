package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.CarRequest.FeatureRequest;
import com.example.car_rental.dto.responseDTO.CarResponse.FeatureResponse;
import com.example.car_rental.dto.responseDTO.CategoryResponse;
import com.example.car_rental.entity.Features;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.FeatureRepository;
import com.example.car_rental.service.FeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements FeatureService {

    private final FeatureRepository featureRepository;

    @Override
    @Transactional
    public void addFeature(FeatureRequest request) {
        if (featureRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.FEATURE_EXISTED);
        }

        Features features = Features.builder()
                .name(request.getName())
                .build();

        featureRepository.save(features);
    }

    @Override
    @Transactional
    public void updateFeature(Long featureId, FeatureRequest request) {
        Features features = featureRepository.findById(featureId)
                .orElseThrow(() -> new AppException(ErrorCode.FEATURE_NOT_FOUND));

        if (request.getName() != null && !request.getName().isEmpty()) {
            if (featureRepository.existsByName(request.getName())) {
                throw new AppException(ErrorCode.FEATURE_EXISTED);
            }
            features.setName(request.getName());
        }

        featureRepository.save(features);
    }

    @Override
    @Transactional
    public void deleteFeature(Long featureId) {
        Features features = featureRepository.findById(featureId)
                .orElseThrow(() -> new AppException(ErrorCode.FEATURE_NOT_FOUND));
        featureRepository.delete(features);
    }

    @Override
    public Page<FeatureResponse> getAllFeature(int page, int size, String name) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Features> carFeatures;

        if (name != null && !name.isEmpty()) {
            carFeatures = featureRepository.findByName(name, pageable);
        } else {
            carFeatures = featureRepository.findAll(pageable);
        }

        return carFeatures.map(features -> FeatureResponse.builder()
                .id(features.getId())
                .name(features.getName())
                .build());
    }

}
