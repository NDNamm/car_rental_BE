package com.example.car_rental.dto.requestDTO.BookingsRequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingRequest {
    private String customerPhone;
    private String customerEmail;
    private String customerName;
    private String nameStaff;
    private String note;
    private LocalDateTime paymentDeadline;
    private List<BookingDetailRequest> bookingDetailRequest;
}
