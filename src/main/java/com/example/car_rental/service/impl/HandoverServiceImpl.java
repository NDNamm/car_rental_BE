package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.handoverRequest.HandoverRequest;
import com.example.car_rental.dto.responseDTO.HandoverResponse;
import com.example.car_rental.entity.*;
import com.example.car_rental.entity.enums.CarHandoverEnum;
import com.example.car_rental.entity.enums.NotificationTypeEnum;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.*;
import com.example.car_rental.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HandoverServiceImpl implements HandoverService {

    private final HandoverRepository handoverRepository;
    private final ImageService imageService;
    private final IncidentService incidentService;
    private final BookingDetailRepository bookingDetailRepository;
    private final InvoiceService invoiceService;

    @Override
    @Transactional
    public void processHandover(Long bookingDetailId, Users staff, HandoverRequest request, MultipartFile[] images) {

        BookingDetails detail = bookingDetailRepository.findById(bookingDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_DETAIL_NOT_FOUND));

        Handovers handover = Handovers.builder()
                        .bookingDetails(detail)
                        .staff(staff)
                        .type(request.getType())
                        .handoverTime(LocalDateTime.now())
                        .mileage(request.getMileage())
                        .fuel_level(request.getFuelLevel())
                        .note(request.getNoteHandover())
                        .createdAt(LocalDateTime.now())
                        .build();

        handoverRepository.save(handover);

        String folderName = request.getType() == CarHandoverEnum.OUT
                        ? "Ảnh bàn giao xe"
                        : "Ảnh thu hồi xe";

        List<HandoverImages> handoverImages = imageService.uploadHandoverImages(images, "handovers", detail.getBooking().getBookingCode(), folderName, handover);

        handover.setHandoverImages(handoverImages);
        handoverRepository.save(handover);


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

            invoiceService.recalculateInvoice(detail.getBooking().getId());
        }
    }

    @Override
    public List<HandoverResponse> getHandoversByBookingDetailId(Long bookingDetailId) {

        List<Handovers> handovers = handoverRepository.findByBookingDetailId(bookingDetailId);

        return handovers.stream()
                .map(handover -> HandoverResponse.builder()
                        .id(handover.getId())
                        .type(handover.getType())
                        .handoverTime(handover.getHandoverTime())
                        .mileage(handover.getMileage())
                        .fuelLevel(handover.getFuel_level())
                        .note(handover.getNote())
                        .createdAt(handover.getCreatedAt())
                        .nameStaff(handover.getStaff().getFullName())
                        .handoverImages(
                                handover.getHandoverImages()
                                        .stream()
                                        .map(HandoverImages::getImageUrl)
                                        .toList()
                        )
                        .incidentResponses(incidentService.getIncidentByHandoverId(handover.getId()))
                        .build())
                .toList();
    }

}
