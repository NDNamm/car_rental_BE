package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.RefundRequest;
import com.example.car_rental.dto.responseDTO.RefundResponse;
import com.example.car_rental.entity.Bookings;
import com.example.car_rental.entity.Refunds;
import com.example.car_rental.entity.Users;
import com.example.car_rental.entity.enums.BookingStatusEnum;
import com.example.car_rental.entity.enums.RefundStatus;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.BookingsRepository;
import com.example.car_rental.repository.RefundRepository;
import com.example.car_rental.repository.UserRepository;
import com.example.car_rental.service.ImageService;
import com.example.car_rental.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    final RefundRepository refundRepository;
    final BookingsRepository bookingsRepository;
    final ImageService imageService;
    final UserRepository userRepository;

    @Override
    public RefundResponse cancelRefund(Long bookingId, RefundRequest request, MultipartFile qrCode) {

        Bookings booking = bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime bookingStart = booking.getCreatedAt();
        long hoursBefore = Duration.between(LocalDateTime.now(), bookingStart).toHours();
        int cancelPercent;
        if (hoursBefore >= 24) {
            cancelPercent = 100;
        } else if (hoursBefore >= 12) {
            cancelPercent = 60;
        } else {
            cancelPercent = 0;
        }

        if (booking.getStatus() == BookingStatusEnum.PENDING) {
            booking.setStatus(BookingStatusEnum.CANCELLED);
            return null;
        } else if (booking.getStatus() == BookingStatusEnum.RENTED || booking.getStatus() == BookingStatusEnum.COMPLETED || booking.getStatus() == BookingStatusEnum.CANCELLED) {
            throw new AppException(ErrorCode.CAR_ALREADY_PICKED_UP_CANNOT_CANCEL);
        } else {
            Refunds refund = Refunds.builder()
                    .booking(booking)
                    .bookingCode(booking.getBookingCode())
                    .hoursBefore(hoursBefore)
                    .cancelPercent(cancelPercent)
                    .amount(calculateCancelAmount(booking, cancelPercent))
                    .status(RefundStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .build();

            try {

                Map<String, String> qrCodeUrl = imageService.uploadImages(qrCode, "users", user.getFullName(), "QrCode");
                refund.setQrCodeUrl(qrCodeUrl.get("url"));
                refund.setPublicId(qrCodeUrl.get("public_id"));
            } catch (IOException e) {
                throw new AppException(ErrorCode.UPDATE_IMAGE_FAIL);
            }

            return getRefund(refund);
        }
    }

    RefundResponse getRefund(Refunds refunds){

        return  RefundResponse.builder()
                .id(refunds.getId())
                .bookingCode(refunds.getBookingCode())
                .amount(refunds.getAmount())
                .hoursBefore(refunds.getHoursBefore())
                .cancelPercent(refunds.getCancelPercent())
                .status(refunds.getStatus())
                .note(refunds.getNote())
                .qrCodeUrl(refunds.getQrCodeUrl())
                .adminProofImage(refunds.getPublicIdProof())
                .createdAt(refunds.getCreatedAt())
                .build();
    }

    public BigDecimal calculateCancelAmount(Bookings booking, int cancelPercent) {
        BigDecimal percent = BigDecimal.valueOf(cancelPercent);
        BigDecimal divisor = new BigDecimal("100");

        // Công thức: (Tiền cọc * Phần trăm) / 100
        BigDecimal refundAmount = booking.getTotalDepositAmount()
                .multiply(percent)
                .divide(divisor, 2, RoundingMode.HALF_UP);

        return refundAmount;
    }

    @Override
    public RefundResponse updateStatus(Long bookingId, RefundRequest request) {
        return null;
    }
}
