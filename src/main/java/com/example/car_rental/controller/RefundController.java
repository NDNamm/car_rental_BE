package com.example.car_rental.controller;

import com.example.car_rental.dto.requestDTO.RefundRequest;
import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.RefundResponse;
import com.example.car_rental.service.RefundService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/refunds")
public class RefundController {

    private final RefundService refundService;

    @GetMapping("")
    public ApiResponse<Page<RefundResponse>> getAllRefundResponse(@RequestParam(defaultValue = "0", required = false) int page,
                                                                  @RequestParam(defaultValue = "5") int size,
                                                                  @RequestParam(required = false) String bookingCode,
                                                                  @RequestParam(required = false) String status) {
        return ApiResponse.<Page<RefundResponse>>builder()
                .code(200)
                .data(refundService.getAllRefund(bookingCode, status, page, size))
                .build();
    }

    @PutMapping(value = "/{refundId}")
    public ApiResponse<RefundResponse> updateRefundResponse(@PathVariable(value = "refundId") Long refundId,
                                                            @RequestPart(value = "refundRequest") String requestJson,
                                                            @RequestPart MultipartFile image) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        RefundRequest request = objectMapper.readValue(requestJson, RefundRequest.class);

        return ApiResponse.<RefundResponse>builder()
                .code(200)
                .data(refundService.updateStatus(refundId, request, image))
                .message("Update refund successful")
                .build();
    }

    @PutMapping(value = "/me/cancel/{bookingId}")
    public ApiResponse<RefundResponse> cancelRefund(@PathVariable(value = "bookingId") Long bookingId,
                                                    @RequestPart(value = "refundRequest", required = false) String requestJson,
                                                    @RequestPart(value = "qrCode") MultipartFile qrCode) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        RefundRequest request = objectMapper.readValue(requestJson, RefundRequest.class);

        return ApiResponse.<RefundResponse>builder()
                .code(200)
                .data(refundService.cancelRefund(bookingId, request, qrCode))
                .message("Update refund successful")
                .build();
    }

    @GetMapping("/me/{refundId}")
    public ApiResponse<RefundResponse> getRefundResponse(@PathVariable(value = "refundId") Long refundId) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return ApiResponse.<RefundResponse>builder()
                .code(200)
                .data(refundService.getRefundUser(email, refundId))
                .build();
    }

}
