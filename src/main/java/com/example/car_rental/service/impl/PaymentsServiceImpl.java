package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.paymentsRequest.SePayWebhookRequest;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingCodeResponse;
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

        // 1. Verify API KEY
//        if (apiKeyHeader == null || !apiKeyHeader.equals(sepayApiKey)) {
//            throw new AppException(ErrorCode.UNAUTHORIZED_WEBHOOK);
//        }
        // 2. Extract booking code
        String bookingCode = extractBookingCode(request.getContent());
        if (bookingCode == null) {
            log.warn("Không tìm thấy bookingCode từ content: {}", request.getContent());
            return;
        }
        bookingCode = normalizeBookingCode(bookingCode);
        // 3. Find booking
        Bookings booking = bookingsRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));
        // 4. Ignore nếu đã xử lý rồi
        if (booking.getStatus() != BookingStatusEnum.PENDING) {
            log.info("Booking {} đã được xử lý trước đó.", bookingCode);
            return;
        }
        // 5. Check timeout thanh toán
        if (LocalDateTime.now().isAfter(booking.getPaymentDeadline())) {
            booking.setStatus(BookingStatusEnum.CANCELLED);
            booking.getBookingDetailsList().forEach(detail ->
                    detail.getCar().setStatus(CarStatusEnum.AVAILABLE)
            );
            Users systemUser = userRepository.findByEmail("system@local")
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            BookingStatusHistory history = BookingStatusHistory.builder()
                    .booking(booking)
                    .newStatus(BookingStatusEnum.CANCELLED)
                    .reason("Hết hạn thanh toán - Auto cancel")
                    .changedAt(LocalDateTime.now())
                    .changedBy(systemUser)
                    .build();
            bookingStatusHistoryRepository.save(history);
            bookingsRepository.save(booking);
            log.warn("Booking {} bị cancel do quá hạn thanh toán", bookingCode);
            return;
        }
        // 6. SAFE NULL CHECK amount
        if (request.getTransferAmount() == null) {
            log.warn("SePay webhook thiếu amount. Content={}", request.getContent());
            return;
        }
        // 7. Compare amount
        if (request.getTransferAmount().compareTo(booking.getTotalDepositAmount()) < 0) {
            log.warn("Số tiền không đủ cho booking {}. Amount={}", bookingCode, request.getTransferAmount());
            return;
        }
        // 8. Find payment
        Payments payment = paymentsRepository
                .findByBookingAndPaymentType(booking, PaymentTypeEnum.DEPOSIT)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));
        // 9. Update payment
        payment.setStatus(PaymentStatusEnum.SUCCESS);
        payment.setTransactionCode(request.getReferenceCode());
        payment.setPaymentMethod(PaymentMethodEnum.BANK_TRANSFER);
        // 10. Update booking
        booking.setStatus(BookingStatusEnum.CONFIRMED);
        // 11. Save
        paymentsRepository.save(payment);
        bookingsRepository.save(booking);

        log.info("Thanh toán cọc thành công cho booking: {}", bookingCode);
    }

    @Override
    public BookingCodeResponse getQrUrlByBookingCode(String bookingCode) {
        Bookings booking = bookingsRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        if (booking.getStatus() != BookingStatusEnum.PENDING) {
            throw new AppException(ErrorCode.BOOKING_ALREADY_PROCESSED);
        }

        return BookingCodeResponse.builder()
                .qrCodeUrl(booking.getQrCodeUrl())
                .bookingCode(booking.getBookingCode())
                .build();
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

        Pattern pattern = Pattern.compile("(BK[_A-Z0-9]+)");
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    private String normalizeBookingCode(String code) {
        if (code == null) return null;

        // nếu dạng BK123ABC → chuyển thành BK_123ABC
        if (code.matches("^BK[0-9A-Z]+$")) {
            return "BK_" + code.substring(2);
        }

        return code;
    }
}
