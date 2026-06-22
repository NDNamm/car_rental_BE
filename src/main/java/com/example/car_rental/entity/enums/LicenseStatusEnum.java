package com.example.car_rental.entity.enums;

import lombok.Getter;

@Getter
public enum LicenseStatusEnum {
    VALID,
    EXPIRING_SOON,
    EXPIRED,
    NULL;
}
