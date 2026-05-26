package com.example.car_rental.controller.DashboardController;

import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.CarDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse;
import com.example.car_rental.service.DashboardService.CarDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard/cars")
public class CarDashboardController {

    private final CarDashboardService carDashboardService;

    @GetMapping("/top-selling")
    public ApiResponse<Page<CarDashboardResponse>> getTopProduct(@RequestParam(defaultValue = "0", required = false) int page,
                                                                 @RequestParam(defaultValue = "5") int size) {
        ApiResponse<Page<CarDashboardResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(carDashboardService.getTopProduct(page, size));
        return apiResponse;
    }

    @GetMapping("/total-revenue")
    public ApiResponse<Page<CarDashboardResponse>> getTotalRevenue(@RequestParam(defaultValue = "0", required = false) int page,
                                                                   @RequestParam(defaultValue = "5") int size) {
        ApiResponse<Page<CarDashboardResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(carDashboardService.getTopCarRevenue(page, size));
        return apiResponse;
    }

    @GetMapping("/total")
    public ApiResponse<Long> getTotalCars() {
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setData(carDashboardService.getTotalCars());
        return apiResponse;
    }

    @GetMapping("/status-count")
    public ApiResponse<List<StatusCountResponse>> getCarStatusCount() {
        ApiResponse<List<StatusCountResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(carDashboardService.getCarStatusCount());
        return apiResponse;
    }


}
