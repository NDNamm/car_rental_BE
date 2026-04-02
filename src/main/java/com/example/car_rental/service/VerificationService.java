package com.example.car_rental.service;


import com.example.car_rental.dto.requestDTO.VerificationRequest.UpdateVerificationStatusRequest;
import com.example.car_rental.dto.requestDTO.VerificationRequest.VerificationRequest;
import com.example.car_rental.dto.responseDTO.VerificationResponse;
import com.example.car_rental.entity.Verifications;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface VerificationService {
    //ADMIN
    Page<VerificationResponse> getAllVerifications(String fullName, String email, String status, int page, int size);
    void updateStatus(Long id, UpdateVerificationStatusRequest request);

    //USER
    VerificationResponse getVerUser();

    void updateVer(VerificationRequest request, MultipartFile cccdFront, MultipartFile cccdBack, MultipartFile driverLicense);
}