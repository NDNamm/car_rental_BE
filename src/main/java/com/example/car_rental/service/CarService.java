package com.example.car_rental.service;

import com.example.car_rental.dto.requestDTO.CarRequest.CarRequest;
import com.example.car_rental.dto.requestDTO.CarRequest.UpdateCarStatusRequest;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingTimeResponse;
import com.example.car_rental.dto.responseDTO.CarResponse.CarResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface CarService {
    void addCar(CarRequest request, MultipartFile[] images);
    void updateCar(Long carId, CarRequest request, MultipartFile[] images);
    void deleteCar(Long carId);
    void updateStatusCar(Long carId, UpdateCarStatusRequest request);
    Page<CarResponse> getAllCar(String name,String status ,int page, int size);
    CarResponse getCarById(Long carId);
    Page<CarResponse> getAllCarsByCategoryId(Long categoryId, int page, int size);
    List<BookingTimeResponse> getBookedRanges(Long carId);
}
