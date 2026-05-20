package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.IncidentRequest;
import com.example.car_rental.entity.*;
import com.example.car_rental.entity.enums.IncidentEnum;
import com.example.car_rental.entity.enums.SourceEnum;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.*;
import com.example.car_rental.service.IncidentService;
import com.example.car_rental.service.PenaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;
    private final BookingDetailRepository bookingDetailRepository;
    private final PenaltyService penaltyService;
    private final PenaltyRuleRepository penaltyRuleRepository;
    private final UserRepository usersRepository;
    private final CarHandoverRepository carHandoverRepository;

    @Override
    public Incidents createAutoIncident(Long handoverId, Users staff, IncidentEnum type, String description) {

        CarHandovers carHandovers = carHandoverRepository.findById(handoverId)
                .orElseThrow(() -> new AppException(ErrorCode.HANDOVER_NOT_FOUND));

        Incidents incidents = Incidents.builder()
                .handover(carHandovers)
                .createdBy(staff)
                .type(type)
                .source(SourceEnum.AUTO)
                .description(description)
                .createdAt(LocalDateTime.now())
                .build();

        return incidentRepository.save(incidents);
    }

    @Override
    public void handleLateReturn(Long handoverId) {
        Users staff = usersRepository.findByEmail("system@local")
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        CarHandovers carHandovers = carHandoverRepository.findById(handoverId)
                .orElseThrow(() -> new AppException(ErrorCode.HANDOVER_NOT_FOUND));

        LocalDateTime expected = carHandovers.getBookingDetails().getEndDate();

        LocalDateTime actual = LocalDateTime.now();

        if (!actual.isAfter(expected)) {
            return;
        }

        long lateMinutes = Duration.between(expected, actual).toMinutes();
        handleLateMinutes(lateMinutes, carHandovers , staff);

    }

//    @Override
//    public void handleExceededMileage(Long bookingDetailId, Long currentMileageLong ) {
//        BookingDetails detail = bookingDetailRepository.findById(bookingDetailId)
//                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_DETAIL_NOT_FOUND));
//
//    }
//
//    @Override
//    public void handleFuelLoss(Long bookingDetailId,  Integer currentFuel) {
//        Users staff = usersRepository.findByEmail("system@local")
//                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//
//        BookingDetails detail = bookingDetailRepository.findById(bookingDetailId)
//                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_DETAIL_NOT_FOUND));
//
//    }

    public void handleLateMinutes(long lateMinutes,CarHandovers carHandovers, Users staff) {
        BigDecimal amount;
        String reason;
        if (lateMinutes < 60) {
            return;
        }

        if (lateMinutes <= 180) {
            PenaltyRules rule = penaltyRuleRepository.findById(1L)
                    .orElseThrow(() -> new AppException(ErrorCode.PENALTY_RULES_NOT_FOUND));

            long lateHours = (long) Math.ceil(lateMinutes / 60.0);
            amount = rule.getUnitPrice().multiply(BigDecimal.valueOf(lateHours));
            reason = "Trả xe quá " + lateHours + " giờ";
        } else {
            long extraDays = (long) Math.ceil(lateMinutes / 1440.0);
            if (extraDays == 0) {
                extraDays = 1;
            }

            BigDecimal dailyRate = carHandovers.getBookingDetails().getCar().getPricePerDay();
            amount = dailyRate.multiply(BigDecimal.valueOf(extraDays));
            reason = "Trả xe quá hạn " + extraDays + " ngày";
        }

        Incidents incident = createAutoIncident(carHandovers.getId(), staff, IncidentEnum.LATE_RETURN, reason);

        penaltyService.createPenalty(incident.getId(), amount);
    }

    @Override
    public void createManualIncident(Long handoverId, List<IncidentRequest> requests, Users staff) {
        CarHandovers carHandovers = carHandoverRepository.findById(handoverId)
                .orElseThrow(() -> new AppException(ErrorCode.HANDOVER_NOT_FOUND));

        for (IncidentRequest request : requests) {
            Incidents incidents = Incidents.builder()
                    .handover(carHandovers)
                    .createdBy(staff)
                    .type(request.getType())
                    .source(SourceEnum.MANUAL)
                    .description(request.getDescription())
                    .createdAt(LocalDateTime.now())
                    .build();

            incidentRepository.save(incidents);

            penaltyService.createPenalty(incidents.getId(), request.getAmount());
        }
    }
}

