package com.example.car_rental.dto.responseDTO.BookingResponse;

import com.example.car_rental.entity.enums.BookingStatusEnum;
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
public class BookingsResponse {
    private Long id;
    private String bookingCode;
    private String customerPhone;
    private String customerEmail;
    private String customerName;
    private BigDecimal totalPrice;
    private BigDecimal totalDepositAmount;
    private BookingStatusEnum status;
    private LocalDateTime createdAt;
    private String nameStaff;
    private String note;
    private LocalDateTime paymentDeadline;
    private List<BookingDetailResponse> bookingDetailsResponse;
    private List<BookingStatusResponse> bookingStatusResponses;
}
