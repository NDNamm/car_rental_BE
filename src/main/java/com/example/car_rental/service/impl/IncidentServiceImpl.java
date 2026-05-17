package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.IncidentRequest;
import com.example.car_rental.entity.*;
import com.example.car_rental.entity.enums.IncidentEnum;
import com.example.car_rental.entity.enums.SourceEnum;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.BookingDetailRepository;
import com.example.car_rental.repository.IncidentRepository;
import com.example.car_rental.repository.PenaltyRuleRepository;
import com.example.car_rental.repository.UserRepository;
import com.example.car_rental.service.IncidentService;
import com.example.car_rental.service.PenaltyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class IncidentServiceImpl implements IncidentService {

    private final IncidentRepository incidentRepository;
    private final BookingDetailRepository bookingDetailRepository;
    private final PenaltyService penaltyService;
    private final PenaltyRuleRepository penaltyRuleRepository;
    private final UserRepository usersRepository;

    @Override
    public Incidents createAutoIncident(Long bookingDetailId, Users staff, IncidentEnum type, String description) {
        BookingDetails bookingDetail = bookingDetailRepository.findById(bookingDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_DETAIL_NOT_FOUND));

        Incidents incidents = Incidents.builder()
                .bookingDetail(bookingDetail)
                .createdBy(staff)
                .type(type)
                .source(SourceEnum.AUTO)
                .description(description)
                .createdAt(LocalDateTime.now())
                .build();

        return incidentRepository.save(incidents);
    }

    @Override
    public void handleLateReturn(Long bookingDetailId) {
        Users staff = usersRepository.findByEmail("system@local")
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        BookingDetails detail = bookingDetailRepository.findById(bookingDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_DETAIL_NOT_FOUND));

        LocalDateTime expected = detail.getEndDate();

        LocalDateTime actual = LocalDateTime.now();

        if (!actual.isAfter(expected)) {
            return;
        }

        long lateMinutes = Duration.between(expected, actual).toMinutes();
        handleLateMinutes(lateMinutes, detail, staff);

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

    public void handleLateMinutes(long lateMinutes, BookingDetails detail, Users staff) {
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

            amount = detail.getCar().getDepositAmount().multiply(BigDecimal.valueOf(extraDays));
            reason = "Trả xe quá hạn " + extraDays + " ngày";
        }

        Incidents incident = createAutoIncident(detail.getId(), staff, IncidentEnum.LATE_RETURN, reason);

        penaltyService.createPenalty(incident.getId(), amount);
    }

    @Override
    public void createManualIncident(Long bookingDetailId, IncidentRequest request, Users staff) {
        BookingDetails detail = bookingDetailRepository.findById(bookingDetailId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_DETAIL_NOT_FOUND));

        Incidents incident = Incidents.builder()
                .bookingDetail(detail)
                .createdBy(staff)
                .type(request.getType())
                .source(SourceEnum.MANUAL)
                .description(request.getDescription())
                .type(request.getType())
                .createdAt(LocalDateTime.now())
                .build();

        incidentRepository.save(incident);

        penaltyService.createPenalty(incident.getId(), request.getAmount());
    }

}
