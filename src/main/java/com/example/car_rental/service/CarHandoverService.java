package com.example.car_rental.service;

import com.example.car_rental.dto.requestDTO.BookingsRequest.BookingStatusRequest;
import com.example.car_rental.dto.requestDTO.CarRequest.CarRequest;
import com.example.car_rental.dto.requestDTO.CarRequest.UpdateCarStatusRequest;
import com.example.car_rental.dto.requestDTO.handoverRequest.HandoverRequest;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingTimeResponse;
import com.example.car_rental.dto.responseDTO.CarResponse.CarResponse;
import com.example.car_rental.entity.BookingDetails;
import com.example.car_rental.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface CarHandoverService {
    void createHandover(BookingDetails detail, Users staff, HandoverRequest request, MultipartFile[] images);

    void returnCar(BookingDetails detail, Users staff, HandoverRequest request, MultipartFile[] images);
}
