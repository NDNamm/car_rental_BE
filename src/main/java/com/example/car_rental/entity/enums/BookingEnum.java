package com.example.car_rental.entity.enums;

import lombok.Getter;

@Getter
public enum BookingEnum {
    PENDING,
    CONFIRMED,
    RENTED,
    COMPLETED,
    CANCELLED;
}
