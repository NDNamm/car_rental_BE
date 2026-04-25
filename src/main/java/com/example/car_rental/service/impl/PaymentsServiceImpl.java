package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.paymentsRequest.SePayWebhookRequest;
import com.example.car_rental.entity.BookingStatusHistory;
import com.example.car_rental.entity.Bookings;
import com.example.car_rental.entity.Payments;
import com.example.car_rental.entity.Users;
import com.example.car_rental.entity.enums.*;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.BookingStatusHistoryRepository;
import com.example.car_rental.repository.BookingsRepository;
import com.example.car_rental.repository.PaymentsRepository;
import com.example.car_rental.repository.UserRepository;
import com.example.car_rental.service.PaymentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentsServiceImpl implements PaymentsService {

    private final BookingsRepository bookingsRepository;
    private final PaymentsRepository paymentsRepository;
    private final BookingStatusHistoryRepository bookingStatusHistoryRepository;
    private final UserRepository userRepository;

    @Value("${sepay.webhook.api-key}")
    private String sepayApiKey;

    @Override
    public String generateQrUrl(Bookings booking) {
        String bankId = "MB";
        String accountNo = "0936106205";
        return String.format(
                "https://img.vietqr.io/image/%s-%s-compact2.png?amount=%s&addInfo=%s",
                bankId, accountNo,
                booking.getTotalDepositAmount().toPlainString(),
                booking.getBookingCode()
        );
    }

    @Override
    @Transactional
    public void processSePayWebhook(SePayWebhookRequest request, String apiKeyHeader) {
        if (apiKeyHeader == null || !apiKeyHeader.equals(sepayApiKey)) {
            throw new AppException(ErrorCode.UNAUTHORIZED_WEBHOOK);
        }

        String bookingCode = extractBookingCode(request.getContent());
        if (bookingCode == null) return;

        Bookings booking = bookingsRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        if (booking.getStatus() != BookingStatusEnum.PENDING) {
            log.info("Booking {} đã được xử lý trước đó.", bookingCode);
            return;
        }

        if (LocalDateTime.now().isAfter(booking.getPaymentDeadline())) {

            booking.setStatus(BookingStatusEnum.CANCELLED);

            booking.getBookingDetailsList().forEach(detail -> {
                detail.getCar().setStatus(CarStatusEnum.AVAILABLE);
            });

            Users user = userRepository.findByEmail("system@local")
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            BookingStatusHistory his = BookingStatusHistory.builder()
                    .booking(booking)
                    .newStatus(BookingStatusEnum.CANCELLED)
                    .reason("Hết hạn thanh toán - Auto cancel")
                    .changedAt(LocalDateTime.now())
                    .changedBy(user)
                    .build();

            bookingStatusHistoryRepository.save(his);
            bookingsRepository.save(booking);

            log.warn("Booking {} bị cancel do thanh toán quá hạn", bookingCode);

            return;
        }

        if (request.getAmount().compareTo(booking.getTotalDepositAmount()) >= 0) {

            // Tìm bản ghi thanh toán cọc đang chờ (PENDING)
            Payments payment = paymentsRepository.findByBookingAndPaymentType(booking, PaymentTypeEnum.DEPOSIT)
                    .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

            payment.setStatus(PaymentStatusEnum.SUCCESS);
            payment.setTransactionCode(request.getReferenceCode());
            payment.setPaymentMethod(PaymentMethodEnum.BANK_TRANSFER);
            booking.setStatus(BookingStatusEnum.CONFIRMED);

            paymentsRepository.save(payment);
            bookingsRepository.save(booking);

            log.info("Xác nhận thành công tiền cọc cho đơn hàng: {}", bookingCode);
        }
    }

    @Override
    public String getQrUrlByBookingCode(String bookingCode) {
        Bookings booking = bookingsRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        if (booking.getStatus() != BookingStatusEnum.PENDING) {
            throw new AppException(ErrorCode.BOOKING_ALREADY_PROCESSED);
        }

        return booking.getQrCodeUrl();
    }

    @Override
    public String checkStatus(String bookingCode) {
        Bookings booking = bookingsRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        return booking.getStatus().name();
    }

    //Lay ma don hang
    private String extractBookingCode(String content) {
        if (content == null) return null;
        Pattern pattern = Pattern.compile("BK_[A-Z0-9]+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        return matcher.find() ? matcher.group().toUpperCase() : null;
    }
}
