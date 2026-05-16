package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.IncidentRequest;
import com.example.car_rental.entity.BookingDetails;
import com.example.car_rental.entity.Incidents;
import com.example.car_rental.entity.Penalties;
import com.example.car_rental.entity.Users;
import com.example.car_rental.entity.enums.IncidentEnum;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.BookingDetailRepository;
import com.example.car_rental.repository.IncidentRepository;
import com.example.car_rental.service.IncidentService;
import com.example.car_rental.service.PenaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;
    private final BookingDetailRepository bookingDetailRepository;

    @Override
    public void createAutoIncident(Long bookingDetailId, Users staff, IncidentRequest request) {

        BookingDetails bookingDetail = bookingDetailRepository.findById(bookingDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_DETAIL_NOT_FOUND));

        Incidents incidents = Incidents.builder()
                .bookingDetail(bookingDetail)
                .createdBy(staff)
                .type(request.getType())
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .build();

        incidentRepository.save(incidents);
    }
}
