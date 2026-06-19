package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.IncidentRequest;
import com.example.car_rental.dto.responseDTO.IncidentResponse;
import com.example.car_rental.entity.*;
import com.example.car_rental.entity.enums.IncidentEnum;
import com.example.car_rental.entity.enums.NotificationTypeEnum;
import com.example.car_rental.entity.enums.SourceEnum;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.*;
import com.example.car_rental.service.IncidentService;
import com.example.car_rental.service.NotificationService;
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
    private final PenaltyService penaltyService;
    private final UserRepository usersRepository;
    private final HandoverRepository handoverRepository;
    private final NotificationService notificationService;

    @Override
    public List<IncidentResponse> getIncidentByHandoverId(Long handoverId) {
        List<Incidents> incidentsList = incidentRepository.findByHandoverId(handoverId);
        return incidentsList.stream().map(incident -> IncidentResponse.builder()
                .id(incident.getId())
                .type(incident.getType())
                .source(incident.getSource())
                .description(incident.getDescription())
                .amount(incident.getPenalties().getAmount())
                .createdAt(incident.getCreatedAt())
                .build()).toList();
    }

    @Override
    public Incidents createAutoIncident(Long handoverId, Users staff, IncidentEnum type, String description) {

        Handovers handovers = handoverRepository.findById(handoverId)
                .orElseThrow(() -> new AppException(ErrorCode.HANDOVER_NOT_FOUND));

        Incidents incidents = Incidents.builder()
                .handover(handovers)
                .createdBy(staff)
                .type(type)
                .source(SourceEnum.AUTO)
                .description(description)
                .createdAt(LocalDateTime.now())
                .build();

        incidentRepository.save(incidents);
        createIncidentNotification(handovers.getBookingDetails().getBooking().getUser().getId(), incidents,
                handovers.getBookingDetails().getBooking().getId());
        return incidents;

    }

    @Override
    public void handleLateReturn(Long handoverId) {
        Users staff = usersRepository.findByEmail("system@gmail.com")
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Handovers handovers = handoverRepository.findById(handoverId)
                .orElseThrow(() -> new AppException(ErrorCode.HANDOVER_NOT_FOUND));

        LocalDateTime expected = handovers.getBookingDetails().getEndDate();

        LocalDateTime actual = LocalDateTime.now();

        if (!actual.isAfter(expected)) {
            return;
        }

        long lateMinutes = Duration.between(expected, actual).toMinutes();
        handleLateMinutes(lateMinutes, handovers, staff);

    }

    // @Override
    // public void handleExceededMileage(Long bookingDetailId, Long
    // currentMileageLong ) {
    // BookingDetails detail = bookingDetailRepository.findById(bookingDetailId)
    // .orElseThrow(() -> new AppException(ErrorCode.BOOKING_DETAIL_NOT_FOUND));
    //
    // }
    //
    // @Override
    // public void handleFuelLoss(Long bookingDetailId, Integer currentFuel) {
    // Users staff = usersRepository.findByEmail("system@gmail.com")
    // .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    //
    // BookingDetails detail = bookingDetailRepository.findById(bookingDetailId)
    // .orElseThrow(() -> new AppException(ErrorCode.BOOKING_DETAIL_NOT_FOUND));
    //
    // }

    public void handleLateMinutes(long lateMinutes, Handovers handovers, Users staff) {
        BigDecimal amount;
        String reason;
        if (lateMinutes < 60) {
            return;
        }

        if (lateMinutes <= 180) {

            BigDecimal hourlyFine = BigDecimal.valueOf(100000);
            long lateHours = (long) Math.ceil(lateMinutes / 60.0);
            amount = hourlyFine.multiply(BigDecimal.valueOf(lateHours));
            reason = "Trả xe quá " + lateHours + " giờ";
        } else {
            long extraDays = (long) Math.ceil(lateMinutes / 1440.0);
            if (extraDays == 0) {
                extraDays = 1;
            }

            BigDecimal dailyRate = handovers.getBookingDetails().getCar().getPricePerDay();
            amount = dailyRate.multiply(BigDecimal.valueOf(extraDays));
            reason = "Trả xe quá hạn " + extraDays + " ngày";
        }

        Incidents incident = createAutoIncident(handovers.getId(), staff, IncidentEnum.LATE_RETURN, reason);

        penaltyService.createPenalty(incident.getId(), amount);
    }

    @Override
    public void createManualIncident(Long handoverId, List<IncidentRequest> requests, Users staff) {
        Handovers handovers = handoverRepository.findById(handoverId)
                .orElseThrow(() -> new AppException(ErrorCode.HANDOVER_NOT_FOUND));

        for (IncidentRequest request : requests) {
            Incidents incidents = Incidents.builder()
                    .handover(handovers)
                    .createdBy(staff)
                    .type(request.getType())
                    .source(SourceEnum.MANUAL)
                    .description(request.getDescription())
                    .createdAt(LocalDateTime.now())
                    .build();

            incidentRepository.save(incidents);

            penaltyService.createPenalty(incidents.getId(), request.getAmount());
            createIncidentNotification(handovers.getBookingDetails().getBooking().getUser().getId(), incidents,
                    handovers.getBookingDetails().getBooking().getId());
        }
    }

    private void createIncidentNotification(Long userId, Incidents incident, Long bookingId) {
        String message = "Sự cố " + incident.getType()
                + " đã được ghi nhận trong quá trình bàn giao xe. Vui lòng kiểm tra chi tiết và xử lý kịp thời.";
        notificationService.createNotification(userId, NotificationTypeEnum.INCIDENT, message, bookingId);
    }
}
