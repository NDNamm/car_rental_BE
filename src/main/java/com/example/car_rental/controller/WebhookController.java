package com.example.car_rental.controller;

import com.example.car_rental.dto.requestDTO.paymentsRequest.SePayWebhookRequest;
import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.service.PaymentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/webhooks")
@RequiredArgsConstructor
@Slf4j
public class WebhookController {

    private final PaymentsService paymentsService;

    @PostMapping("/sepay")
    public ApiResponse<Void> handleSePayWebhook(
            @RequestBody SePayWebhookRequest request,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        log.info(">>> Nhận Webhook từ SePay: Content='{}', Amount={}",
                request.getContent(), request.getTransferAmount());

        String apiKey = "";
        if (authHeader != null) {
            apiKey = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        }

        paymentsService.processSePayWebhook(request, apiKey);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Webhook processed")
                .build();
    }
}