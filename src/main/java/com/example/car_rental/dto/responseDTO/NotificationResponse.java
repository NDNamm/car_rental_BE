package com.example.car_rental.dto.responseDTO;

import com.example.car_rental.entity.Users;
import com.example.car_rental.entity.enums.NotificationTypeEnum;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private Long id;
    private Long referenceId;
    private NotificationTypeEnum type;
    private String message;
    private Boolean isRead = false;
    private LocalDateTime createdAt;
    private Users user;

}
