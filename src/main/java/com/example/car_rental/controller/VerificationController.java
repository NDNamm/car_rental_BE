package com.example.car_rental.controller;

import com.example.car_rental.dto.requestDTO.VerificationRequest.UpdateVerificationStatusRequest;
import com.example.car_rental.dto.requestDTO.VerificationRequest.VerificationRequest;
import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.car_rental.dto.responseDTO.VerificationResponse;
import com.example.car_rental.service.VerificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/verification")
public class VerificationController {

    private final VerificationService verificationService;

    //ADMIN
    @GetMapping("")
    public ApiResponse<Page<VerificationResponse>> getAllVerificationUser(@RequestParam(required = false) String email,
                                                                    @RequestParam(required = false) String fullName,
                                                                    @RequestParam(required = false) String status,
                                                                    @RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int size) {
        Page<VerificationResponse> verification = verificationService.getAllVerifications(email, fullName, status, page, size);

        return ApiResponse.<Page<VerificationResponse>>builder()
                .code(200)
                .message("Get verification successful")
                .data(verification)
                .build();
    }

    @PutMapping("/{verId}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Long verId,
                                          @RequestBody UpdateVerificationStatusRequest request) {
        verificationService.updateStatus(verId, request);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Verification status updated successfully")
                .build();
    }


    //CLIENT
    @GetMapping("/me")
    public ApiResponse<VerificationResponse> getVerificationUser() {
        VerificationResponse verification = verificationService.getVerUser();

        return ApiResponse.<VerificationResponse>builder()
                .code(200)
                .message("Get verification successful")
                .data(verification)
                .build();
    }

    @PostMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Void> update(@RequestPart(value = "verificationRequest") String requestJson,
                                    @RequestPart(value = "cccdFront") MultipartFile cccdFront,
                                    @RequestPart(value = "cccdBack") MultipartFile cccdBack,
                                    @RequestPart(value = "license") MultipartFile license) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        VerificationRequest request = objectMapper.readValue(requestJson, VerificationRequest.class);
        verificationService.updateVer(request, cccdFront, cccdBack, license);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Update verification successful")
                .build();
    }


}
