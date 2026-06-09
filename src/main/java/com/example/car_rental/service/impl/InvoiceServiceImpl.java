package com.example.car_rental.service.impl;

import com.example.car_rental.dto.responseDTO.InvoicesResponse;
import com.example.car_rental.entity.Bookings;
import com.example.car_rental.entity.Invoices;
import com.example.car_rental.entity.enums.BookingStatusEnum;
import com.example.car_rental.entity.enums.InvoiceStatusEnum;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.BookingsRepository;
import com.example.car_rental.repository.InvoiceRepository;
import com.example.car_rental.repository.PenaltyRepository;
import com.example.car_rental.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    final private BookingsRepository bookingsRepository;
    final private InvoiceRepository invoiceRepository;
    final private PenaltyRepository penaltyRepository;

    @Override
    public InvoicesResponse getInvoiceByBookingId(Long bookingId) {

        Invoices invoice = invoiceRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_FOUND));

        return mapToResponse(invoice);
    }

    @Override
    public void generateInvoice(Long bookingId) {
        Bookings booking = bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        Invoices invoices = Invoices.builder()
                .booking(booking)
                .status(InvoiceStatusEnum.PENDING)
                .finalAmount(booking.getTotalPrice())
                .createdAt(booking.getCreatedAt())
                .rentalTotal(booking.getTotalPrice())
                .build();

        invoiceRepository.save(invoices);
    }

    @Override
    public void recalculateInvoice(Long bookingId) {
        Bookings booking = bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        Invoices invoice = invoiceRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_FOUND));

        BigDecimal penaltyTotal = penaltyRepository.sumPenaltyByBookingId(bookingId);
        if (penaltyTotal == null) {
            penaltyTotal = BigDecimal.ZERO;
        }

        BigDecimal finalAmount = booking.getTotalPrice().add(penaltyTotal).subtract(booking.getTotalDepositAmount());
        if (finalAmount.compareTo(BigDecimal.ZERO) < 0) {
            finalAmount = BigDecimal.ZERO;
        }

        invoice.setRentalTotal(booking.getTotalPrice());
        invoice.setPenaltyTotal(penaltyTotal);
        invoice.setDepositPaid(booking.getTotalDepositAmount());
        invoice.setFinalAmount(finalAmount);
        invoice.setUpdatedAt(LocalDateTime.now());
        invoice.setPaidTotal(booking.getTotalDepositAmount());
        if (booking.getStatus().equals(BookingStatusEnum.COMPLETED)) {
            invoice.setRemainingAmount(BigDecimal.ZERO);
            invoice.setPaidTotal(finalAmount);
            invoice.setStatus(InvoiceStatusEnum.PAID);
        } else if (booking.getStatus().equals(BookingStatusEnum.CANCELLED)) {
            invoice.setRemainingAmount(finalAmount);
            invoice.setStatus(InvoiceStatusEnum.CANCELLED);
        } else {
            invoice.setRemainingAmount(finalAmount);
        }
        invoice.setNote("Điều chỉnh hóa đơn sau kiểm tra tình trạng xe");
        invoiceRepository.save(invoice);
    }

    @Override
    public void updateInvoiceStatus(Long bookingId, InvoiceStatusEnum status) {
        Invoices invoice = invoiceRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_FOUND));

        invoice.setStatus(status);
        invoiceRepository.save(invoice);

    }

    @Override
    public InvoicesResponse getInvoiceByBookingIdUser(Long bookingId, String email) {
        Invoices invoice = invoiceRepository.findByBookingIdAndUserEmail(bookingId, email)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_FOUND));
        return mapToResponse(invoice);
    }

    public InvoicesResponse mapToResponse(Invoices invoice) {
        return InvoicesResponse.builder()
                .id(invoice.getId())
                .bookingId(invoice.getBooking().getId())
                .bookingCode(invoice.getBooking().getBookingCode())
                .customerName(invoice.getBooking().getCustomerName())
                .customerEmail(invoice.getBooking().getCustomerEmail())
                .customerPhone(invoice.getBooking().getCustomerPhone())
                .rentalTotal(invoice.getRentalTotal())
                .penaltyTotal(invoice.getPenaltyTotal())
                .depositPaid(invoice.getDepositPaid())
                .finalAmount(invoice.getFinalAmount())
                .paidTotal(invoice.getPaidTotal())
                .note(invoice.getNote())
                .remainingAmount(invoice.getRemainingAmount())
                .status(invoice.getStatus())
                .createdAt(invoice.getCreatedAt())
                .build();
    }
}
