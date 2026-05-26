package com.example.car_rental.dto.responseDTO.DashboardResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Builder
public class CustomerResponse {
    private String customerName;
    private String customerEmail;
    private Long totalBookings;
    private BigDecimal totalPrice;

    public CustomerResponse(String customerEmail, String customerName, Long totalBookings, BigDecimal totalPrice) {
        this.customerEmail = customerEmail;
        this.customerName = customerName;
        this.totalBookings = totalBookings;
        this.totalPrice = totalPrice;
    }

//    public CustomerResponse(String customerEmail ,String customerName, BigDecimal totalPrice) {
//        this.customerEmail = customerEmail;
//        this.customerName = customerName;
//        this.totalPrice = totalPrice;
//    }
}
