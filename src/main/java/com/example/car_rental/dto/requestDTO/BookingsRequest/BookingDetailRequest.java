package com.example.car_rental.dto.requestDTO.BookingsRequest;

import com.example.car_rental.dto.requestDTO.CarRequest.CarsRequest;
import com.example.car_rental.dto.responseDTO.CarResponse.CarResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDetailRequest {
    private Long id;
    private Long bookingId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
    private BigDecimal pricePerDay;
    private BigDecimal subTotal;
    private BigDecimal depositAmount;
    private CarsRequest carsRequest;
}
