package com.example.car_rental.service.impl;

import com.example.car_rental.domain.RefundCalculate;
import com.example.car_rental.dto.requestDTO.RefundRequest;
import com.example.car_rental.dto.responseDTO.RefundResponse;
import com.example.car_rental.entity.Bookings;
import com.example.car_rental.entity.Payments;
import com.example.car_rental.entity.Refunds;
import com.example.car_rental.entity.Users;
import com.example.car_rental.entity.enums.*;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.BookingsRepository;
import com.example.car_rental.repository.PaymentsRepository;
import com.example.car_rental.repository.RefundRepository;
import com.example.car_rental.repository.UserRepository;
import com.example.car_rental.service.ImageService;
import com.example.car_rental.service.NotificationService;
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
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    final RefundRepository refundRepository;
    final ImageService imageService;
    final PaymentsRepository paymentsRepository;
    final RefundCalculate refundCalculate;
    final NotificationService notificationService;
    final UserRepository userRepository;

    @Override
    public Page<RefundResponse> getAllRefundUser(String email, String bookingCode, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Refunds> refundsPage = null;

        boolean noFilter = (bookingCode == null || bookingCode.isBlank()) &&
                (status == null || status.isBlank());

        if (noFilter) {
            refundsPage = refundRepository.findRefundByUser(email, null, null, pageable);
        } else {
            RefundStatus statusEnum = null;

            if (status != null && !status.isBlank()) {
                statusEnum = RefundStatus.valueOf(status.toUpperCase());
            }
            refundsPage = refundRepository.findRefundByUser(email, bookingCode, statusEnum, pageable);
        }

        return refundsPage.map(this::getRefund);
    }

    @Override
    @Transactional
    public RefundResponse createRefund(Bookings booking, RefundRequest request, MultipartFile qrCode) {

        if (refundRepository.existsByBooking(booking)) {
            throw new AppException(ErrorCode.REFUND_ALREADY_EXISTS);
        }
        if (booking.getStatus() != BookingStatusEnum.CONFIRMED) {
            throw new AppException(ErrorCode.INVALID_BOOKING_STATUS);
        }
        // ❗ check payment tồn tại + đã thanh toán
        Payments payment = paymentsRepository
                .findByBookingAndPaymentType(booking, PaymentTypeEnum.DEPOSIT)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

        if (payment.getStatus() != PaymentStatusEnum.SUCCESS) {
            throw new AppException(ErrorCode.PAYMENT_NOT_COMPLETED);
        }

        if (booking.getBookingDetailsList() == null || booking.getBookingDetailsList().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_BOOKING_TIME);
        }
        LocalDateTime bookingStart = booking.getBookingDetailsList().get(0).getStartDate();

        long hoursBefore = refundCalculate.calculateHoursBefore(bookingStart);
        int refundPercent = refundCalculate.calculateRefundPercent(hoursBefore);

        Refunds refund = Refunds.builder()
                .booking(booking)
                .bookingCode(booking.getBookingCode())
                .reason(request.getReason())
                .hoursBefore(hoursBefore)
                .refundPercent(refundPercent)
                .amount(refundCalculate.calculateRefundAmount(booking, refundPercent))
                .status(RefundStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        try {
            Map<String, String> qrCodeUrl = imageService.uploadImages(qrCode, "bookings", booking.getBookingCode(),
                    "QrCode");

            refund.setQrCodeUrl(qrCodeUrl.get("url"));
            refund.setPublicId(qrCodeUrl.get("public_id"));

        } catch (IOException e) {
            throw new AppException(ErrorCode.UPDATE_IMAGE_FAIL);
        }

        refundRepository.save(refund);
        createBookingNotification(booking.getUser(), RefundStatus.PENDING, booking.getBookingCode(), refund.getId());
        createBookingNotificationAdmin(booking.getBookingCode(), refund.getId());
        return getRefund(refund);
    }

    RefundResponse getRefund(Refunds refunds) {

        return RefundResponse.builder()
                .id(refunds.getId())
                .bookingCode(refunds.getBookingCode())
                .customerName(refunds.getBooking().getCustomerName())
                .customerEmail(refunds.getBooking().getCustomerEmail())
                .reason(refunds.getReason())
                .amount(refunds.getAmount())
                .hoursBefore(refunds.getHoursBefore())
                .refundPercent(refunds.getRefundPercent())
                .status(refunds.getStatus())
                .note(refunds.getNote())
                .qrCodeUrl(refunds.getQrCodeUrl())
                .adminProofImage(refunds.getAdminProofImage())
                .createdAt(refunds.getCreatedAt())
                .build();
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
                            + " (" + refunds.getRefundPercent() + "%)"
                            + " số tiền sau " + refunds.getHoursBefore() + " giờ cọc");

            try {
                Map<String, String> adminProofImageUrl = imageService.uploadImages(
                        adminProofImage,
                        "bookings",
                        refunds.getBookingCode(),
                        "adminProofImage");

                refunds.setAdminProofImage(adminProofImageUrl.get("url"));
                refunds.setPublicIdProof(adminProofImageUrl.get("public_id"));

            } catch (IOException e) {
                throw new AppException(ErrorCode.UPDATE_IMAGE_FAIL);
            }

        } else if (request.getStatus() == RefundStatus.REJECTED) {
            refunds.setStatus(RefundStatus.REJECTED);
            refunds.setNote(request.getNote());

        }

        refundRepository.save(refunds);
        createBookingNotification(refunds.getBooking().getUser(), refunds.getStatus(), refunds.getBookingCode(),
                refundId);
        return getRefund(refunds);
    }

    @Override
    public Page<RefundResponse> getAllRefund(String bookingCode, String status, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Refunds> refundsPage;

        boolean noFilter = (bookingCode == null || bookingCode.isBlank()) &&
                (status == null || status.isBlank());

        if (noFilter) {
            refundsPage = refundRepository.findByBookingCode(null, null, pageable);
        } else {
            RefundStatus statusEnum = null;

            if (status != null && !status.isBlank()) {
                statusEnum = RefundStatus.valueOf(status.toUpperCase());
            }
            refundsPage = refundRepository.findByBookingCode(bookingCode, statusEnum, pageable);
        }

        return refundsPage.map(this::getRefund);
    }

    private void createBookingNotification(Users user, RefundStatus status, String bookingCode, Long refundId) {
        String message = switch (status) {
            case REJECTED -> "Yêu cầu hoàn tiền đơn đặt xe:  " + bookingCode + " của bạn đã bị từ chối.";
            case COMPLETED -> "Đã hoàn tiền thành công đơn đặt xe:  " + bookingCode;
            default ->
                "Bạn đã yêu cầu hoàn tiền đơn đặt xe:  " + bookingCode
                        + ". Chúng tôi sẽ xem xét và phản hồi sớm nhất có thể.";
        };
        notificationService.createNotification(user.getId(), NotificationTypeEnum.REFUND, message, refundId);
    }

    private void createBookingNotificationAdmin(String bookingCode, Long refundId) {
        List<Users> admins = userRepository.findByRole_RoleName("ADMIN");
        List<Users> staffs = userRepository.findByRole_RoleName("STAFF");
        for (Users adminUser : admins) {
            notificationService.createNotification(adminUser.getId(), NotificationTypeEnum.REFUND,
                    "Yêu cầu hoàn tiền đơn đặt xe:  " + bookingCode
                            + " đang chờ xử lý. Vui lòng kiểm tra và cập nhật trạng thái.",
                    refundId);
        }

        for (Users staff : staffs) {
            notificationService.createNotification(staff.getId(), NotificationTypeEnum.REFUND,
                    "Yêu cầu hoàn tiền đơn đặt xe:  " + bookingCode
                            + " đang chờ xử lý. Vui lòng kiểm tra và cập nhật trạng thái.",
                    refundId);
        }
    }
}
