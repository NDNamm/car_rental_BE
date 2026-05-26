package com.example.car_rental.service.impl.BookingDashboardImpl;

import com.example.car_rental.dto.responseDTO.DashboardResponse.CarDashboardResponse;
import com.example.car_rental.dto.responseDTO.DashboardResponse.StatusCountResponse;
import com.example.car_rental.repository.DashboardRepository.CarDashboardRepository;
import com.example.car_rental.service.DashboardService.CarDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarDashboardServiceImpl implements CarDashboardService {

    private final CarDashboardRepository carDashboardRepository;

    @Override
    public Page<CarDashboardResponse> getTopProduct(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return carDashboardRepository.getTopCars(pageable);
    }

    @Override
    public Page<CarDashboardResponse> getTopCarRevenue(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return carDashboardRepository.getTopRevenueCars(pageable);
    }

    @Override
    public Long getTotalCars() {
        return carDashboardRepository.getTotalCars();
    }

    @Override
    public List<StatusCountResponse> getCarStatusCount() {
        return carDashboardRepository.getCarStatusStatistics();
    }


}
