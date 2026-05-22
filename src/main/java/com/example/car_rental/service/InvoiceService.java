package com.example.car_rental.service;

import com.example.car_rental.dto.responseDTO.InvoicesResponse;
import com.example.car_rental.entity.enums.InvoiceStatusEnum;

public interface InvoiceService {
    InvoicesResponse getInvoiceByBookingId(Long bookingId);
    void generateInvoice(Long bookingId);
    void recalculateInvoice(Long bookingId);
    void updateInvoiceStatus(Long bookingId, InvoiceStatusEnum status);

    InvoicesResponse getInvoiceByBookingIdUser(Long bookingId, String email);
}