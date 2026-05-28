package com.example.car_rental.dto.responseDTO.DashboardResponse.Customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class CustomerTopResponse  {
    private String customerEmail;
    private String customerName;
    private Long totalBookings;
    private BigDecimal totalSpending;

}
