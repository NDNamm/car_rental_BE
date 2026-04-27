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
import jakarta.transaction.Transactional;
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
    public RefundResponse getRefundUser(String email, Long refundId) {
        Refunds refunds = refundRepository.findById(refundId)
                .orElseThrow(() -> new AppException(ErrorCode.REFUND_NOT_FOUND));

        return getRefund(refunds);
    }

    @Override
    @Transactional
    public RefundResponse cancelRefund(Long bookingId, RefundRequest request, MultipartFile qrCode) {

        Bookings booking = bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

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
                    .reason(request.getReason())
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

            refundRepository.save(refund);

            booking.setStatus(BookingStatusEnum.CANCELLED);
            bookingsRepository.save(booking);
            return getRefund(refund);
        }
    }

    RefundResponse getRefund(Refunds refunds) {

        return RefundResponse.builder()
                .id(refunds.getId())
                .bookingCode(refunds.getBookingCode())
                .customerName(refunds.getBooking().getCustomerName())
                .reason(refunds.getReason())
                .amount(refunds.getAmount())
                .hoursBefore(refunds.getHoursBefore())
                .cancelPercent(refunds.getCancelPercent())
                .status(refunds.getStatus())
                .note(refunds.getNote())
                .qrCodeUrl(refunds.getQrCodeUrl())
                .adminProofImage(refunds.getAdminProofImage())
                .createdAt(refunds.getCreatedAt())
                .build();
    }

    public BigDecimal calculateCancelAmount(Bookings booking, int cancelPercent) {
        BigDecimal percent = BigDecimal.valueOf(cancelPercent);
        BigDecimal divisor = new BigDecimal("100");

        // Công thức: (Tiền cọc * Phần trăm) / 100
        BigDecimal cancelAmount = booking.getTotalDepositAmount()
                .multiply(percent)
                .divide(divisor, 2, RoundingMode.HALF_UP);

        BigDecimal refundAmount = booking.getTotalDepositAmount().subtract(cancelAmount);
        return refundAmount;
    }

    @Override
    @Transactional
    public RefundResponse updateStatus(Long refundId, RefundRequest request, MultipartFile adminProofImage) {

        Refunds refunds = refundRepository.findById(refundId)
                .orElseThrow(() -> new AppException(ErrorCode.REFUND_NOT_FOUND));

        if (refunds.getStatus() == RefundStatus.COMPLETED) {
            throw new AppException(ErrorCode.REFUND_ALREADY_COMPLETED);
        }

        if (request.getStatus() == RefundStatus.COMPLETED) {

            if (adminProofImage == null) {
                throw new AppException(ErrorCode.IMAGE_REQUIRED);
            }

            refunds.setStatus(RefundStatus.COMPLETED);

            refunds.setNote(
                    "Đã hoàn " + refunds.getAmount()
                            + " (" + refunds.getCancelPercent() + "%)"
                            + " số tiền sau " + refunds.getHoursBefore() + " giờ cọc"
            );

            try {
                Map<String, String> adminProofImageUrl = imageService.uploadImages(
                        adminProofImage,
                        "bookings",
                        refunds.getBookingCode(),
                        "adminProofImage"
                );

                refunds.setAdminProofImage(adminProofImageUrl.get("url"));
                refunds.setPublicIdProof(adminProofImageUrl.get("public_id"));

            } catch (IOException e) {
                throw new AppException(ErrorCode.UPDATE_IMAGE_FAIL);
            }

        }

        else if (request.getStatus() == RefundStatus.REJECTED) {

            refunds.setStatus(RefundStatus.REJECTED);
            refunds.setNote(request.getNote());

        }

        refundRepository.save(refunds);

        return getRefund(refunds);
    }

    @Override
    public Page<RefundResponse> getAllRefund(String bookingCode, String status, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Refunds> responsePage;

        if (bookingCode == null || bookingCode.isEmpty()) {
            responsePage = refundRepository.findAll(pageable);
        } else {
            responsePage = refundRepository.findByBookingCode(bookingCode,status, pageable);
        }
        return responsePage.map(this::getRefund);
    }
}
