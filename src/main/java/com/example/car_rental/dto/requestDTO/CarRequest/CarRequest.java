package com.example.car_rental.dto.requestDTO.CarRequest;


import com.example.car_rental.entity.enums.CarStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarRequest {
    private Long id;
    private String name;
    private String licensePlate;
    private BigDecimal pricePerDay;
    private BigDecimal depositAmount;
    private CarStatusEnum status;
    private String description;
    private int tripCount;
    private Long categoryId;
    private CarDetailRequest carDetailRequest;
}
