package com.example.car_rental.entity.enums;

import lombok.Getter;

@Getter
public enum BookingStatusEnum {
    PENDING,
    CONFIRMED,
    RENTED,
    COMPLETED,
    CANCELLED,
    REFUND;
}
