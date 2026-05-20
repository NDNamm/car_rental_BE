package com.example.car_rental.entity.enums;

import lombok.Getter;

@Getter
public enum IncidentEnum {
    SCRATCH,
    DIRTY,
    LATE_RETURN,
    FUEL_LOSS,
    ACCIDENT,
    ODO_EXCEEDED,
    TIRE_DAMAGE,
    INTERIOR_DAMAGE,
    OTHER;
}
