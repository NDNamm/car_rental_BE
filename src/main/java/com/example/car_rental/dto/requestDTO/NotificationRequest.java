package com.example.car_rental.dto.requestDTO;

import com.example.car_rental.entity.enums.NotificationTypeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;



@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationRequest {

    private NotificationTypeEnum type;
    private String message;

}
