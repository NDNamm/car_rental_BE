package com.example.car_rental.dto.requestDTO.BookingsRequest;

import com.example.car_rental.dto.responseDTO.BookingResponse.BookingDetailResponse;
import com.example.car_rental.entity.enums.BookingStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingsRequest {

    private String bookingCode;
    private String customerPhone;
    private String customerEmail;
    private String customerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalPrice;
    private BigDecimal depositAmount;
    private BookingStatusEnum status;
    private LocalDate createdAt;
    private String nameStaff;
    private List<BookingDetailResponse> bookingDetailsResponse;
}
