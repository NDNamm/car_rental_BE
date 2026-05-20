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
import com.example.car_rental.service.IncidentService;
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
    private final IncidentService incidentService;
    private final BookingDetailRepository bookingDetailRepository;

    @Override
    @Transactional
    public void processHandover(Long bookingDetailId, Users staff, HandoverRequest request, MultipartFile[] images) {

        BookingDetails detail = bookingDetailRepository.findById(bookingDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_DETAIL_NOT_FOUND));

        CarHandovers handover = CarHandovers.builder()
                        .bookingDetails(detail)
                        .staff(staff)
                        .type(request.getType())
                        .handoverTime(LocalDateTime.now())
                        .mileage(request.getMileage())
                        .fuel_level(request.getFuelLevel())
                        .note(request.getNoteHandover())
                        .createdAt(LocalDateTime.now())
                        .build();

        carHandoverRepository.save(handover);

        String folderName = request.getType() == CarHandoverEnum.OUT
                        ? "Ảnh bàn giao xe"
                        : "Ảnh thu hồi xe";

        List<HandoverImages> handoverImages = imageService.uploadHandoverImages(images, "handovers", detail.getBooking().getBookingCode(), folderName, handover);

        handover.setHandoverImages(handoverImages);
        carHandoverRepository.save(handover);


        if (request.getType() == CarHandoverEnum.IN) {
            incidentService.handleLateReturn(handover.getId());
//            incidentService.handleExceededMileage(bookingDetailId, request.getMileage());
//            incidentService.handleFuelLoss(bookingDetailId, request.getFuelLevel());

            if (request.getIncidentRequests() != null) {
                incidentService.createManualIncident(
                        handover.getId(),
                        request.getIncidentRequests(),
                        staff
                );
            }
        }
    }
}
