package com.example.car_rental.service;

import com.example.car_rental.dto.requestDTO.paymentsRequest.SePayWebhookRequest;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingCodeResponse;
import com.example.car_rental.entity.Bookings;

public interface PaymentsService {

    public void processSePayWebhook(SePayWebhookRequest request, String apiKeyHeader);
    public String generateQrUrl(Bookings booking);
    BookingCodeResponse getQrUrlByBookingCode(String bookingCode);
    String checkStatus(String bookingCode);
}
