package com.example.car_rental.dto.responseDTO.BookingResponse;

import com.example.car_rental.dto.responseDTO.CarResponse.CarResponse;
import com.example.car_rental.dto.responseDTO.HandoverResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDetailResponse {
    private Long id;
    private Long rentalDays;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal pricePerDay;
    private BigDecimal subTotal;
    private BigDecimal depositAmount;
    private CarResponse carResponse;
    private List<HandoverResponse> handoverResponses;
}
