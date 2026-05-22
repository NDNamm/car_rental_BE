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
import com.example.car_rental.service.InvoiceService;
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
    private final InvoiceService invoiceService;

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
    public void createPayment(Bookings booking) {
        Payments payment = Payments.builder()
                .booking(booking)
                .amount(booking.getTotalDepositAmount())
                .status(PaymentStatusEnum.UNPAID)
                .paymentType(PaymentTypeEnum.DEPOSIT)
                .paymentMethod(PaymentMethodEnum.BANK_TRANSFER)
                .paymentContent(booking.getBookingCode())
                .createdAt(LocalDateTime.now())
                .build();

        paymentsRepository.save(payment);
    }

    @Override
    @Transactional
    public void processSePayWebhook(SePayWebhookRequest request, String apiKeyHeader) {

        String bookingCode = extractBookingCode(request.getContent());
        if (bookingCode == null) {
            log.warn("Không tìm thấy bookingCode từ content: {}", request.getContent());
            return;
        }
        bookingCode = normalizeBookingCode(bookingCode);
        Bookings booking = bookingsRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));
        //Ignore nếu đã xử lý rồi
        if (booking.getStatus() != BookingStatusEnum.PENDING) {
            log.info("Booking {} đã được xử lý trước đó.", bookingCode);
            return;
        }
        //Check timeout thanh toán
        if (LocalDateTime.now().isAfter(booking.getPaymentDeadline())) {
            booking.setStatus(BookingStatusEnum.CANCELLED);
            booking.getBookingDetailsList().forEach(detail ->
                    detail.getCar().setStatus(CarStatusEnum.AVAILABLE)
            );
            bookingsRepository.save(booking);

            createBookingHistory(booking);
            invoiceService.updateInvoiceStatus(booking.getId(), InvoiceStatusEnum.CANCELLED);
            log.warn("Booking {} bị cancel do quá hạn thanh toán", bookingCode);
            return;
        }
        //SAFE NULL CHECK amount
        if (request.getTransferAmount() == null) {
            log.warn("SePay webhook thiếu amount. Content={}", request.getContent());
            return;
        }
        //Compare amount
        if (request.getTransferAmount().compareTo(booking.getTotalDepositAmount()) < 0) {
            log.warn("Số tiền không đủ cho booking {}. Amount={}", bookingCode, request.getTransferAmount());
            return;
        }
        //Find payment
        Payments payment = paymentsRepository
                .findByBookingAndPaymentType(booking, PaymentTypeEnum.DEPOSIT)
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_NOT_FOUND));

        payment.setStatus(PaymentStatusEnum.SUCCESS);
        payment.setTransactionCode(request.getReferenceCode());
        payment.setPaymentMethod(PaymentMethodEnum.BANK_TRANSFER);
        paymentsRepository.save(payment);

        booking.setStatus(BookingStatusEnum.CONFIRMED);
        bookingsRepository.save(booking);

        createBookingHistory(booking);
        invoiceService.updateInvoiceStatus(booking.getId(), InvoiceStatusEnum.PARTIAL);
        invoiceService.recalculateInvoice(booking.getId());

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

    private void createBookingHistory(Bookings booking) {
        Users systemUser = userRepository.findByEmail("system@local")
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        BookingStatusEnum newStatus = booking.getStatus();
        String reason;

        if(newStatus == BookingStatusEnum.CONFIRMED) {
            reason = "Thanh toán cọc thành công";
        } else if (newStatus == BookingStatusEnum.CANCELLED) {
            reason = "Hết hạn thanh toán - Auto cancel";
        } else {
            reason = "Cập nhật trạng thái tự động";
        }
        BookingStatusHistory history = BookingStatusHistory.builder()
                .booking(booking)
                .newStatus(newStatus)
                .reason(reason)
                .changedAt(LocalDateTime.now())
                .changedBy(systemUser)
                .build();
        bookingStatusHistoryRepository.save(history);
    }
}
