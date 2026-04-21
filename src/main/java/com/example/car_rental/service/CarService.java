package com.example.car_rental.service;

import com.example.car_rental.dto.requestDTO.CarRequest.CarsRequest;
import com.example.car_rental.dto.responseDTO.CarResponse.CarResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;


public interface CarService {
    void addCar(CarsRequest request, MultipartFile[] images);
    void updateCar(Long carId, CarsRequest request, MultipartFile[] images);
    void deleteCar(Long carId);
    void updateStatusCar(Long carId, String status);
    Page<CarResponse> getAllCar(String name,String status ,int page, int size);
    CarResponse getCarById(Long carId);
    Page<CarResponse> getAllCarsByCategoryId(Long categoryId, int page, int size);
}
