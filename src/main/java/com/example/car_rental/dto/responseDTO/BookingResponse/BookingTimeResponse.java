package com.example.car_rental.dto.responseDTO.BookingResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingTimeResponse {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}