package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.VerificationRequest.UpdateVerificationStatusRequest;
import com.example.car_rental.dto.requestDTO.VerificationRequest.VerificationRequest;
import com.example.car_rental.dto.responseDTO.UserResponse;
import com.example.car_rental.dto.responseDTO.VerificationResponse;
import com.example.car_rental.entity.Users;
import com.example.car_rental.entity.Verifications;
import com.example.car_rental.entity.enums.LicenseStatusEnum;
import com.example.car_rental.entity.enums.NotificationTypeEnum;
import com.example.car_rental.entity.enums.VerStatusEnum;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.UserRepository;
import com.example.car_rental.repository.VerificationRepository;
import com.example.car_rental.service.ImageService;
import com.example.car_rental.service.NotificationService;
import com.example.car_rental.service.VerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class VerificationServiceImpl implements VerificationService {

    private final VerificationRepository verificationRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    final private NotificationService notificationService;

    // ADMIN
    @Override
    public Page<VerificationResponse> getAllVerifications(String email, String fullName, String status, int page,
            int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Verifications> versPage = null;

        boolean noFilter = (email == null || email.isBlank()) &&
                (fullName == null || fullName.isBlank()) &&
                (status == null || status.isBlank());

        if (noFilter) {
            versPage = verificationRepository.findAllVerification(null, null, null, pageable);
        } else {
            VerStatusEnum statusEnum = null;

            if (status != null && !status.isBlank()) {
                statusEnum = VerStatusEnum.valueOf(status.toUpperCase());
            }
            versPage = verificationRepository.findAllVerification(email, fullName, statusEnum, pageable);
        }

        return versPage.map(this::mapToVerification);
    }

    @Override
    @Transactional
    public void updateStatus(Long verId, UpdateVerificationStatusRequest request) {

        Verifications verification = verificationRepository.findById(verId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_VERIFIED));

        VerStatusEnum newStatus = request.getStatus();
        verification.setStatus(newStatus);
        verification.setNote(request.getNote());
        if (newStatus == VerStatusEnum.APPROVED) {
            verification.setLicenseStatus(LicenseStatusEnum.VALID);
        } else {
            verification.setLicenseStatus(LicenseStatusEnum.NULL);
        }
        verificationRepository.save(verification);

        createNotificationVerification(verification.getUser().getId(), newStatus, verId);

    }

    // Client
    @Override
    public VerificationResponse getVerUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Verifications verification = verificationRepository.findByUserId(user.getId())
                .orElseGet(() -> Verifications.builder()
                        .user(user)
                        .status(VerStatusEnum.PENDING)
                        .build());

        return mapToVerification(verification);
    }

    @Override
    @Transactional
    public void updateVer(VerificationRequest request, MultipartFile cccdFront, MultipartFile cccdBack,
            MultipartFile driverLicense) {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Verifications verification = verificationRepository.findByUserId(user.getId())
                .orElseGet(() -> Verifications.builder()
                        .user(user)
                        .status(VerStatusEnum.PENDING)
                        .build());

        if (verification.getStatus() == VerStatusEnum.APPROVED
                && LicenseStatusEnum.VALID.equals(verification.getLicenseStatus())) {
            throw new AppException(ErrorCode.ALREADY_VERIFIED);
        }

        try {
            if (cccdFront != null && !cccdFront.isEmpty()) {
                if (verification.getCccdFrontUrl() != null && verification.getPublicIdCccdFront() != null) {
                    imageService.deleteImages(verification.getPublicIdCccdFront());
                }

                Map<String, String> cccdFrontUrl = imageService.uploadImages(cccdFront, "users", user.getFullName(),
                        "cccd_front");
                verification.setCccdFrontUrl(cccdFrontUrl.get("url"));
                verification.setPublicIdCccdFront(cccdFrontUrl.get("public_id"));
            }

            if (cccdBack != null && !cccdBack.isEmpty()) {
                if (verification.getCccdBackUrl() != null && verification.getPublicIdCccdBack() != null) {
                    imageService.deleteImages(verification.getPublicIdCccdBack());
                }

                Map<String, String> cccdBackUrl = imageService.uploadImages(cccdBack, "users", user.getFullName(),
                        "cccd_back");
                verification.setCccdBackUrl(cccdBackUrl.get("url"));
                verification.setPublicIdCccdBack(cccdBackUrl.get("public_id"));
            }

            if (driverLicense != null && !driverLicense.isEmpty()) {
                if (verification.getLicenseImageUrl() != null && verification.getPublicIdLicense() != null) {
                    imageService.deleteImages(verification.getLicenseImageUrl());
                }

                Map<String, String> LicenseImageUrl = imageService.uploadImages(driverLicense, "users",
                        user.getFullName(), "driver_license");
                verification.setLicenseImageUrl(LicenseImageUrl.get("url"));
                verification.setPublicIdLicense(LicenseImageUrl.get("public_id"));
            }
        } catch (IOException e) {
            throw new AppException(ErrorCode.UPDATE_IMAGE_FAIL);
        }

        verification.setLicenseNumber(request.getLicenseNumber());
        verification.setLicenseExpiredDate(request.getLicenseExpiredDate());
        verification.setStatus(VerStatusEnum.PENDING);
        verification.setLicenseStatus(LicenseStatusEnum.NULL);
        verification.setCreatedAt(LocalDate.now());

        verificationRepository.save(verification);

        List<Users> admins = userRepository.findByRole_RoleName("ADMIN");
        List<Users> staffs = userRepository.findByRole_RoleName("STAFF");
        for (Users admin : admins) {
            createNotificationVerification(admin.getId(), VerStatusEnum.PENDING, verification.getId());
        }
        for (Users staff : staffs) {
            createNotificationVerification(staff.getId(), VerStatusEnum.PENDING, verification.getId());
        }

    }

    private VerificationResponse mapToVerification(Verifications verification) {
        return VerificationResponse.builder()
                .id(verification.getId())
                .userId(verification.getUser().getId())
                .fullName(verification.getUser().getFullName())
                .email(verification.getUser().getEmail())
                .cccdFrontUrl(verification.getCccdFrontUrl())
                .cccdBackUrl(verification.getCccdBackUrl())
                .licenseImageUrl(verification.getLicenseImageUrl())
                .licenseNumber(verification.getLicenseNumber())
                .status(verification.getStatus())
                .note(verification.getNote())
                .licenseExpiredDate(verification.getLicenseExpiredDate())
                .licenseStatus(verification.getLicenseStatus())
                .createdAt(verification.getCreatedAt())
                .build();
    }

    public void createNotificationVerification(Long userId, VerStatusEnum status, Long verId) {

        String message = switch (status) {
            case APPROVED -> "Tài khoản của bạn đã được xác minh thành công.";
            case REJECTED -> "Tài khoản của bạn đã bị từ chối xác minh. Vui lòng kiểm tra lại thông tin và thử lại.";
            case PENDING -> "Có một yêu cầu xác minh mới đang chờ xử lý. Vui lòng kiểm tra và cập nhật trạng thái.";
            default -> "Xác minh tài khoản để đặt xe";
        };
        notificationService.createNotification(userId, NotificationTypeEnum.VERIFICATION, message, verId);
    }
}
