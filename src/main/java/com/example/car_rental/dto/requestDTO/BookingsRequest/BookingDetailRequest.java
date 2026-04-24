package com.example.car_rental.dto.requestDTO.BookingsRequest;

import com.example.car_rental.dto.requestDTO.CarRequest.CarRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingDetailRequest {
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal pricePerDay;
    private BigDecimal depositAmount;
    private CarRequest carRequest;
}
