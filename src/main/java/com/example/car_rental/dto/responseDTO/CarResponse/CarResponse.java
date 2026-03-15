package com.example.car_rental.dto.responseDTO.CarResponse;

import com.example.car_rental.dto.requestDTO.CarRequest.CarDetailRequest;
import com.example.car_rental.entity.CarImages;
import com.example.car_rental.entity.enums.CarStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarResponse {
    private Long id;
    private String name;
    private String description;
    private String licensePlate;
    private BigDecimal pricePerDay;
    private BigDecimal depositAmount;
    private CarStatusEnum status;
    private int tripCount;
    private LocalDate createdAt;
    private String categoryName;
    private List<CarImageResponse> carImageResponses;
    private CarDetailResponse carDetailResponse;
}
