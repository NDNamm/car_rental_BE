package com.example.car_rental.service.impl;

import com.cloudinary.Cloudinary;
import com.example.car_rental.dto.requestDTO.VerificationRequest;
import com.example.car_rental.dto.responseDTO.VerificationResponse;
import com.example.car_rental.entity.Users;
import com.example.car_rental.entity.Verifications;
import com.example.car_rental.entity.enums.VerStatusEnum;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.UserRepository;
import com.example.car_rental.repository.VerificationRepository;
import com.example.car_rental.service.ImageService;
import com.example.car_rental.service.VerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class VerificationServiceImpl implements VerificationService {

    private final VerificationRepository verificationRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;

    @Override
    public VerificationResponse getVerUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        String email = authentication.getName();
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Verifications verification = verificationRepository.findByUserId(user.getId());

        return VerificationResponse.builder()
                .id(verification.getId())
                .userId(user.getId())
                .cccdFrontUrl(verification.getCccdFrontUrl())
                .cccdBackUrl(verification.getCccdBackUrl())
                .licenseImageUrl(verification.getLicenseImageUrl())
                .publicIdCccdFront(verification.getPublicIdCccdFront())
                .publicIdCccdBack(verification.getPublicIdCccdBack())
                .publicIdLicense(verification.getPublicIdLicense())
                .licenseNumber(verification.getLicenseNumber())
                .status(verification.getStatus())
                .note(verification.getNote())
                .licenseExpiredDate(verification.getLicenseExpiredDate())
                .createdAt(verification.getCreatedAt())
                .build();
    }

    @Override
    @Transactional
    public void updateVer(VerificationRequest request, MultipartFile cccdFront, MultipartFile cccdBack, MultipartFile driverLicense) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        String email = authentication.getName();
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Verifications verification = verificationRepository.findByUserId(user.getId());

        if (verification == null) {
            verification = new Verifications();
            verification.setUser(user);
        }

        if (verification.getStatus() == VerStatusEnum.APPROVED) {
            throw new AppException(ErrorCode.ALREADY_VERIFIED);
        }

        try {
            if (cccdFront != null && !cccdFront.isEmpty()) {
                if (verification.getCccdFrontUrl() != null && verification.getPublicIdCccdFront() != null) {
                    imageService.deleteImages(verification.getPublicIdCccdFront());
                }

                Map<String, String> cccdFrontUrl = imageService.uploadImages(cccdFront, "users", user.getFullName());
                verification.setCccdFrontUrl(cccdFrontUrl.get("url"));
                verification.setPublicIdCccdFront(cccdFrontUrl.get("public_id"));
            }

            if (cccdBack != null && !cccdBack.isEmpty()) {
                if (verification.getCccdBackUrl() != null && verification.getPublicIdCccdBack() != null) {
                    imageService.deleteImages(verification.getPublicIdCccdBack());
                }

                Map<String, String> cccdBackUrl = imageService.uploadImages(cccdBack, "users", user.getFullName());
                verification.setCccdBackUrl(cccdBackUrl.get("url"));
                verification.setPublicIdCccdBack(cccdBackUrl.get("public_id"));
            }

            if (driverLicense != null && !driverLicense.isEmpty()) {
                if (verification.getLicenseImageUrl() != null && verification.getPublicIdLicense() != null) {
                    imageService.deleteImages(verification.getLicenseImageUrl());
                }

                Map<String, String> LicenseImageUrl = imageService.uploadImages(cccdFront, "users", user.getFullName());
                verification.setLicenseImageUrl(LicenseImageUrl.get("url"));
                verification.setPublicIdLicense(LicenseImageUrl.get("public_id"));
            }
        }catch (IOException e){
            throw new AppException(ErrorCode.UPDATE_IMAGE_FAIL);
        }

        verification.setLicenseNumber(request.getLicenseNumber());
        verification.setLicenseExpiredDate(request.getLicenseExpiredDate());
        verification.setStatus(VerStatusEnum.PENDING);
        verification.setCreatedAt(LocalDate.now());

        verificationRepository.save(verification);

    }
}
