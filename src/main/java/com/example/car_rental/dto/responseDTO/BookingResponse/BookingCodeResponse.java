package com.example.car_rental.dto.responseDTO.BookingResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingCodeResponse {
    private String bookingCode;
    private String qrCodeUrl;
}
