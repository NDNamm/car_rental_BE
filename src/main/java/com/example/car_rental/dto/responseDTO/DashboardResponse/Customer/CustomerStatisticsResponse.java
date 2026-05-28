package com.example.car_rental.dto.responseDTO.DashboardResponse.Customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerStatisticsResponse {

    private Long totalCustomers;
    private Long isActive;
    private Long inactiveCustomers;
}
