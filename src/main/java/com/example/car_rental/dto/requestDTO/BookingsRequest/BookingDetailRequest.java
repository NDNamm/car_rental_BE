package com.example.car_rental.dto.requestDTO.BookingsRequest;

import com.example.car_rental.dto.responseDTO.CarResponse.CarResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDetailRequest {
    private Long bookingId;
    private BigDecimal pricePerDay;
    private BigDecimal subTotal;
    private CarResponse carResponses;
}
