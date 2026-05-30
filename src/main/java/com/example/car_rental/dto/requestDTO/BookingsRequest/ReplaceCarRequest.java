package com.example.car_rental.dto.requestDTO.BookingsRequest;

import com.example.car_rental.dto.requestDTO.CarRequest.CarRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReplaceCarRequest {
    private Long bookingDetailId;
    private Long newCarId;
}
