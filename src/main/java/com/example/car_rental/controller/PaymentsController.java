package com.example.car_rental.controller;

import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingCodeResponse;
import com.example.car_rental.service.PaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/payments")
@RequiredArgsConstructor
public class PaymentsController {

    private final PaymentsService paymentsService;

    @GetMapping("/qr/{bookingCode}")
    public ApiResponse<BookingCodeResponse> getPaymentQr(@PathVariable String bookingCode) {

        return ApiResponse.<BookingCodeResponse>builder()
                .code(200)
                .data(paymentsService.getQrUrlByBookingCode(bookingCode))
                .message("QR code generated successfully")
                .build();
    }

    @GetMapping("/status/{bookingCode}")
    public ApiResponse<String> checkPaymentStatus(@PathVariable String bookingCode) {
        String status = paymentsService.checkStatus(bookingCode);

        return ApiResponse.<String>builder()
                .code(200)
                .data(status)
                .message("Payment status retrieved")
                .build();
    }
}