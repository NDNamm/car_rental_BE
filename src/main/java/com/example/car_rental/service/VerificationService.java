package com.example.car_rental.service;


import com.example.car_rental.dto.requestDTO.UserRequest.UserRequest;
import com.example.car_rental.dto.requestDTO.VerificationRequest;
import com.example.car_rental.dto.responseDTO.VerificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface VerificationService {
    //ADMIN


    //USER
    VerificationResponse getVerUser();
    void updateVer(VerificationRequest request, MultipartFile cccdFront, MultipartFile cccdBack, MultipartFile driverLicense);
}