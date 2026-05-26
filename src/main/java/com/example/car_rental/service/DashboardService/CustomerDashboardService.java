package com.example.car_rental.service.DashboardService;

import com.example.car_rental.dto.responseDTO.DashboardResponse.CustomerResponse;
import org.springframework.data.domain.Page;

import java.util.List;


public interface CustomerDashboardService {

    Page<CustomerResponse> getTopCustomers(int page, int size);
    Long getTotalCustomers();
    Page<CustomerResponse> getTopCustomerCancelled(int page, int size);
    Page<CustomerResponse> getTopSpendingCustomers(int page, int size);
}
