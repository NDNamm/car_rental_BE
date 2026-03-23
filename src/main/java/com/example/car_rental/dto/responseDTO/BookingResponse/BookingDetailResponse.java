package com.example.car_rental.dto.responseDTO.BookingResponse;

import com.example.car_rental.dto.responseDTO.CarResponse.CarResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDetailResponse {
    private Long id;
    private Long bookingId;
    private Integer rentalDays;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal pricePerDay;
    private BigDecimal subTotal;
    private BigDecimal depositAmount;
    private CarResponse carResponse;
}
