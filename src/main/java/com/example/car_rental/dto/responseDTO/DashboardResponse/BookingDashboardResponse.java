package com.example.car_rental.dto.responseDTO.DashboardResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDashboardResponse {
    private LocalDate  date;
    private int month;
    private BigDecimal revenue;
    private String status;
    private Long bookingCount;
    private String carName;
    private Long totalCars;

    public BookingDashboardResponse(LocalDateTime date, BigDecimal revenue, Long bookingCount) {
        this.date = date.toLocalDate();
        this.revenue = revenue;
        this.bookingCount = bookingCount;
    }

    public BookingDashboardResponse(String carName, Long totalCars) {
        this.carName = carName;
        this.totalCars = totalCars;
    }

    public BookingDashboardResponse(int month, BigDecimal revenue, Long bookingCount) {
        this.month = month;
        this.revenue = revenue;
        this.bookingCount = bookingCount;
    }
}
