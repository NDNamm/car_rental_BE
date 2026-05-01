package com.example.car_rental.controller;

import com.example.car_rental.dto.requestDTO.RefundRequest;
import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.RefundResponse;
import com.example.car_rental.service.RefundService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
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

    @PutMapping(value = "/{refundId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<RefundResponse> updateRefundResponse(@PathVariable(value = "refundId") Long refundId,
                                                            @RequestPart(value = "refundRequest", required = false) String requestJson,
                                                            @RequestPart(required = false, value = "adminProofImage") MultipartFile adminProofImage) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        RefundRequest request = objectMapper.readValue(requestJson, RefundRequest.class);

        return ApiResponse.<RefundResponse>builder()
                .code(200)
                .data(refundService.updateStatus(refundId, request, adminProofImage))
                .message("Update refund successful")
                .build();
    }


    //USER
    @GetMapping("/me")
    public ApiResponse<Page<RefundResponse>> getRefundUser(@RequestParam(defaultValue = "0", required = false) int page,
                                                     @RequestParam(defaultValue = "5") int size,
                                                     @RequestParam(required = false) String bookingCode,
                                                     @RequestParam(required = false) String status) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return ApiResponse.<Page<RefundResponse>> builder()
                .code(200)
                .data(refundService.getAllRefundUser(email, bookingCode, status, page, size))
                .build();
    }

}
