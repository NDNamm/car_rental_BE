package com.example.car_rental.controller;

import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.InvoicesResponse;
import com.example.car_rental.dto.responseDTO.NotificationResponse;
import com.example.car_rental.service.InvoiceService;
import com.example.car_rental.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    final private NotificationService notificationService;

    @GetMapping("/{userId}")
    public ApiResponse<Page<NotificationResponse>> getAllNotifications(@PathVariable(value = "userId") Long userId,
                                                                       @RequestParam(defaultValue = "0", required = false) int page,
                                                                       @RequestParam(defaultValue = "5") int size) {
        {

            return ApiResponse.<Page<NotificationResponse>>builder()
                    .code(200)
                    .data(notificationService.getNotificationsByUserId(userId, page, size))
                    .build();
        }
    }
}
