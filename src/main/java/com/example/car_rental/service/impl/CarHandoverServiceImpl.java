package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.BookingsRequest.BookingStatusRequest;
import com.example.car_rental.dto.requestDTO.CarRequest.CarDetailRequest;
import com.example.car_rental.dto.requestDTO.CarRequest.CarRequest;
import com.example.car_rental.dto.requestDTO.CarRequest.UpdateCarStatusRequest;
import com.example.car_rental.dto.requestDTO.handoverRequest.HandoverRequest;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingTimeResponse;
import com.example.car_rental.dto.responseDTO.CarResponse.CarDetailResponse;
import com.example.car_rental.dto.responseDTO.CarResponse.CarImageResponse;
import com.example.car_rental.dto.responseDTO.CarResponse.CarResponse;
import com.example.car_rental.entity.*;
import com.example.car_rental.entity.enums.CarHandoverEnum;
import com.example.car_rental.entity.enums.CarStatusEnum;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.*;
import com.example.car_rental.service.CarHandoverService;
import com.example.car_rental.service.CarService;
import com.example.car_rental.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarHandoverServiceImpl implements CarHandoverService {

    private final CarHandoverRepository carHandoverRepository;
    private final ImageService imageService;

    @Override
    @Transactional
    public void createHandover(BookingDetails detail, Users staff, HandoverRequest request, MultipartFile[] images) {

        CarHandovers handover = CarHandovers.builder()
                .bookingDetails(detail)
                .staff(staff)
                .type(CarHandoverEnum.OUT)
                .handoverTime(LocalDateTime.now())
                .mileage(request.getMileage())
                .fuel_level(request.getFuelLevel())
                .note(request.getNoteHandover())
                .createdAt(LocalDateTime.now())
                .build();
        carHandoverRepository.save(handover);

        List<HandoverImages> handoverImages =
                imageService.uploadHandoverImages(images, "handovers", detail.getBooking().getBookingCode(),"Ảnh bàn giao xe" ,handover);

        handover.setHandoverImages(handoverImages);
        carHandoverRepository.save(handover);
    }

    @Override
    public void returnCar(BookingDetails detail, Users staff, HandoverRequest request, MultipartFile[] images) {
        CarHandovers handover = CarHandovers.builder()
                .bookingDetails(detail)
                .staff(staff)
                .type(CarHandoverEnum.IN)
                .handoverTime(LocalDateTime.now())
                .mileage(request.getMileage())
                .fuel_level(request.getFuelLevel())
                .note(request.getNoteHandover())
                .createdAt(LocalDateTime.now())
                .build();
        carHandoverRepository.save(handover);

        List<HandoverImages> handoverImages =
                imageService.uploadHandoverImages(images, "handovers", detail.getBooking().getBookingCode(),"Ảnh thu nhận lại xe" ,handover);

        handover.setHandoverImages(handoverImages);
        carHandoverRepository.save(handover);
    }
}
