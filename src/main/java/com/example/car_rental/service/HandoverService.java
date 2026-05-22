package com.example.car_rental.service;

import com.example.car_rental.dto.requestDTO.handoverRequest.HandoverRequest;
import com.example.car_rental.dto.responseDTO.HandoverResponse;
import com.example.car_rental.entity.Users;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface HandoverService {
    void processHandover(Long bookingDetailId, Users staff, HandoverRequest request, MultipartFile[] images);
    List<HandoverResponse> getHandoversByBookingDetailId(Long bookingDetailId);
}
