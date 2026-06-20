package com.example.car_rental.entity.enums;

import lombok.Getter;

@Getter
public enum IncidentEnum {

    SCRATCH("Trầy xước ngoại thất"),
    DIRTY("Xe bẩn"),
    LATE_RETURN("Trả xe quá hạn"),
    FUEL_LOSS("Thiếu nhiên liệu"),
    ACCIDENT("Tai nạn giao thông"),
    ODO_EXCEEDED("Vượt giới hạn số km"),
    TIRE_DAMAGE("Hư hỏng lốp xe"),
    INTERIOR_DAMAGE("Hư hỏng nội thất"),
    OTHER("Khác");

    private final String label;

    IncidentEnum(String label) {
        this.label = label;
    }
}
