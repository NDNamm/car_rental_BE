package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.NotificationRequest;
import com.example.car_rental.dto.responseDTO.NotificationResponse;
import com.example.car_rental.entity.Notifications;
import com.example.car_rental.entity.Users;
import com.example.car_rental.entity.enums.NotificationTypeEnum;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.NotificationRepository;
import com.example.car_rental.repository.UserRepository;
import com.example.car_rental.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    final private NotificationRepository notificationRepository;
    final private UserRepository userRepository;

    @Override
    public Page<NotificationResponse> getNotificationsByUserId(Long userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Notifications> notificationsPage = notificationRepository.findByUserId(userId, pageable);

        return notificationsPage.map(notification -> NotificationResponse.builder()
                .id(notification.getId())
                .type(notification.getType())
                .message(notification.getMessage())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .referenceId(notification.getReferenceId())
                .build());

    }

    @Override
    public void createNotification(Long userId, NotificationTypeEnum type, String message, Long referenceId) {

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Notifications notification = Notifications.builder()
                .user(user)
                .type(type)
                .message(message)
                .referenceId(referenceId)
                .createdAt(LocalDateTime.now())
                .isRead(false)
                .build();

        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsRead(userId);
    }

    @Override
    @Transactional
    public void markNotificationAsRead(Long notificationId, Long userId) {
        notificationRepository.markAsRead(notificationId, userId);
    }

    @Override
    @Transactional
    public void deleteAllNotification(Long userId) {
        notificationRepository.deleteAllRead(userId);
    }

    @Override
    @Transactional
    public void deleteNotification(Long notificationId, Long userId) {
        notificationRepository.deleteNotification(notificationId, userId);
    }

    @Override
    public Long countUnreadNotifications(Long userId) {
        return notificationRepository.countUnreadNotifications(userId);
    }

}
