package com.example.car_rental.service.impl.BookingDashboardImpl;

import com.example.car_rental.dto.responseDTO.DashboardResponse.CustomerResponse;
import com.example.car_rental.repository.DashboardRepository.CustomerDashboardRepository;
import com.example.car_rental.service.DashboardService.CustomerDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerDashboardServiceImpl implements CustomerDashboardService {

    private final CustomerDashboardRepository customerDashboardRepository;


    @Override
    public Page<CustomerResponse> getTopCustomers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return customerDashboardRepository.getTopCustomers(pageable);
    }

    @Override
    public Long getTotalCustomers() {
        return customerDashboardRepository.getTotalCustomers();
    }

    @Override
    public Page<CustomerResponse> getTopCustomerCancelled(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return customerDashboardRepository.getTopCustomerCancelled(pageable);
    }

    @Override
    public Page<CustomerResponse> getTopSpendingCustomers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return customerDashboardRepository.getTopSpendingCustomers(pageable);
    }
}
