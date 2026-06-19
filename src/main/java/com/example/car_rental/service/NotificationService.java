package com.example.car_rental.service;

import com.example.car_rental.dto.requestDTO.NotificationRequest;
import com.example.car_rental.dto.responseDTO.NotificationResponse;
import com.example.car_rental.entity.enums.NotificationTypeEnum;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NotificationService {
    Page<NotificationResponse> getNotificationsByUserId(Long userId, int page, int size);

    void createNotification(Long userId, NotificationTypeEnum type, String message, Long referenceId);

    void markAllAsRead(Long userId);

    void markNotificationAsRead(Long notificationId, Long userId);

    void deleteAllNotification(Long userId);

    void deleteNotification(Long notificationId, Long userId);

    Long countUnreadNotifications(Long userId);
}
