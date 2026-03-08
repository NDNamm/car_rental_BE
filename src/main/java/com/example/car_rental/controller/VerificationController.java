package com.example.car_rental.controller;

import com.example.car_rental.dto.requestDTO.VerificationRequest;
import com.example.car_rental.dto.requestDTO.authRequest.LoginRequest;
import com.example.car_rental.dto.requestDTO.authRequest.RegisterRequest;
import com.example.car_rental.dto.requestDTO.passwordRequest.ForgotPasswordRequest;
import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.AuthResponse;
import com.example.car_rental.dto.responseDTO.VerificationResponse;
import com.example.car_rental.service.AuthService;
import com.example.car_rental.service.PasswordService;
import com.example.car_rental.service.VerificationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/verification")
public class VerificationController {

    private final VerificationService verificationService;

    @GetMapping("")
    public ApiResponse<VerificationResponse> getVerUser() {
        VerificationResponse verification = verificationService.getVerUser();

        return ApiResponse.<VerificationResponse>builder()
                .code(200)
                .message("Get verification successful")
                .data(verification)
                .build();
    }

    @PostMapping("/update")
    public ApiResponse<Void> update(@RequestPart(value = "updateVer", required = false) VerificationRequest request,
                                    @RequestPart(value = "cccdFront", required = false) MultipartFile cccdFront,
                                    @RequestPart(value = "cccdBack", required = false) MultipartFile cccdBack,
                                    @RequestPart(value = "license", required = false) MultipartFile license) {

        verificationService.updateVer(request, cccdFront, cccdBack, license);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Update verification successful")
                .build();
    }


}
