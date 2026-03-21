package com.example.car_rental.dto.requestDTO.CarRequest;

import com.example.car_rental.entity.CarImages;
import com.example.car_rental.entity.Categories;
import com.example.car_rental.entity.enums.CarStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarsRequest {

    private String name;
    private String licensePlate;
    private BigDecimal pricePerDay;
    private BigDecimal depositAmount;
    private CarStatusEnum status;
    private String description;
    private int tripCount;
    private LocalDate createdAt;
    private Long categoryId;
    private CarDetailRequest carDetailRequest;
}
