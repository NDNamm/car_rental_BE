package com.example.car_rental.entity.enums;

import lombok.Getter;

@Getter
public enum NotificationTypeEnum {
    VERIFICATION,
    BOOKING,
    INVOICE,
    REVIEW,
    REFUND,
    ACCOUNT,
    INCIDENT;
}
