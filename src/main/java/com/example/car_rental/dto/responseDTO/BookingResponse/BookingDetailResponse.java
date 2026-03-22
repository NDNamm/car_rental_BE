package com.example.car_rental.dto.responseDTO.BookingResponse;

import com.example.car_rental.dto.responseDTO.CarResponse.CarResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDetailResponse {
    private Long id;
    private Long bookingId;
    private BigDecimal pricePerDay;
    private BigDecimal subTotal;
    private CarResponse carResponse;
}
