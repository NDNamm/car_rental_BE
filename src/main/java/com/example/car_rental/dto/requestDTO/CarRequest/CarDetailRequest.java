package com.example.car_rental.dto.requestDTO.CarRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDetailRequest {

    private Integer  seatCount;
    private String fuelType;
    private String transmission;
    private Integer  manufactureYear;
    private String address;
    private String city;
    private Boolean deliveryAvailable;
    private BigDecimal deliveryFee;
    private Set<Long> features;
}
