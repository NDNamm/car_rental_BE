package com.example.car_rental.service;


import com.example.car_rental.dto.requestDTO.VerificationRequest.UpdateVerificationStatusRequest;
import com.example.car_rental.dto.requestDTO.VerificationRequest.VerificationRequest;
import com.example.car_rental.dto.responseDTO.VerificationResponse;
import org.springframework.web.multipart.MultipartFile;

public interface VerificationService {
    //ADMIN
    void updateStatus(Long id, UpdateVerificationStatusRequest request);

    //USER
    VerificationResponse getVerUser();

    void updateVer(VerificationRequest request, MultipartFile cccdFront, MultipartFile cccdBack, MultipartFile driverLicense);
}