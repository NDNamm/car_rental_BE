package com.example.car_rental.controller.DashboardController;

import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.BookingDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.CustomerResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse;
import com.example.car_rental.service.DashboardService.BookingDashboardService;
import com.example.car_rental.service.DashboardService.CustomerDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard/customers")
public class CustomerDashboardController {

    private final CustomerDashboardService customerDashboardService;

    @GetMapping("/top")
    public ApiResponse<Page<CustomerResponse>> getTopCustomers(@RequestParam(defaultValue = "0", required = false) int page,
                                                               @RequestParam(defaultValue = "5") int size) {
        ApiResponse<Page<CustomerResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(customerDashboardService.getTopCustomers(page, size));
        return apiResponse;
    }

    @GetMapping("/cancelled")
    public ApiResponse<Page<CustomerResponse>> getTopCustomerCancelled(@RequestParam(defaultValue = "0", required = false) int page,
                                                                       @RequestParam(defaultValue = "5") int size) {
        ApiResponse<Page<CustomerResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(customerDashboardService.getTopCustomerCancelled(page, size));
        return apiResponse;
    }

    @GetMapping("/sending")
    public ApiResponse<Page<CustomerResponse>> getTopSpendingCustomers(@RequestParam(defaultValue = "0", required = false) int page,
                                                                       @RequestParam(defaultValue = "5") int size) {
        ApiResponse<Page<CustomerResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(customerDashboardService.getTopSpendingCustomers(page, size));
        return apiResponse;
    }

    @GetMapping("/total")
    public ApiResponse<Long> getTotalCars() {
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setData(customerDashboardService.getTotalCustomers());
        return apiResponse;
    }

}
