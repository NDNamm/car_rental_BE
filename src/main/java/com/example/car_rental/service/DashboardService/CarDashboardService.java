package com.example.car_rental.service.DashboardService;

import com.example.car_rental.dto.responseDTO.DashboardResponse.CarDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse;
import org.springframework.data.domain.Page;

import java.util.List;


public interface CarDashboardService {

    Page<CarDashboardResponse> getTopProduct(int page, int size);
    Page<CarDashboardResponse> getTopCarRevenue(int page, int size);
    Long getTotalCars();
    List<StatusCountResponse> getCarStatusCount();
}
