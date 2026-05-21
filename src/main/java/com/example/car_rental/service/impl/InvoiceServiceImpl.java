package com.example.car_rental.service.impl;

import com.example.car_rental.dto.responseDTO.InvoicesResponse;
import com.example.car_rental.entity.Bookings;
import com.example.car_rental.entity.Invoices;
import com.example.car_rental.entity.enums.InvoiceStatusEnum;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.BookingsRepository;
import com.example.car_rental.repository.InvoiceRepository;
import com.example.car_rental.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    final private BookingsRepository bookingsRepository;
    final private InvoiceRepository invoiceRepository;

    @Override
    public InvoicesResponse getInvoiceByBookingId(Long bookingId) {
        return null;
    }

    @Override
    public void generateInvoice(Long bookingId) {
        Bookings booking = bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        Invoices invoices = Invoices.builder()
                .booking(booking)
                .status(InvoiceStatusEnum.PENDING)
                .createdAt(booking.getCreatedAt())
                .rentalTotal(booking.getTotalPrice())
                .build();

        invoiceRepository.save(invoices);
    }

    @Override
    public void recalculateInvoice(Long bookingId) {
        Bookings booking = bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        Invoices invoice = invoiceRepository.findByBooking(booking)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_FOUND));

        invoice.setRentalTotal(booking.getTotalPrice());
        invoiceRepository.save(invoice);
    }

    @Override
    public void updateInvoiceStatus(Long bookingId, InvoiceStatusEnum status) {
        Bookings booking = bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        Invoices invoice = invoiceRepository.findByBooking(booking)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_FOUND));

        invoice.setStatus(status);
        invoiceRepository.save(invoice);

    }
}
