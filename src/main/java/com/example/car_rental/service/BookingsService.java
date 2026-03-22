package com.example.car_rental.service;

import com.example.car_rental.dto.requestDTO.BookingsRequest.BookingStatusRequest;
import com.example.car_rental.dto.requestDTO.BookingsRequest.BookingsRequest;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingsResponse;
import org.springframework.data.domain.Page;

public interface BookingsService {

    //ADMIN
    Page<BookingsResponse> getAllBookings(String bookingCode ,String name, String phoneNumber, String status, int page, int size);
    void updateBookingsStatus(BookingStatusRequest request, Long bookingId);
    void deleteBooking(Long bookingId);

    //USER
    Long addBooking(BookingsRequest bookingsRequest);
    void updateBooking(BookingsRequest bookingsRequest, Long bookingId);
    Page<BookingsResponse> getAllBookingUser(String bookingCode,String status, int page, int size);
    void updateBookingUser(BookingsRequest bookingsRequest, Long bookingId);
    void cancelBooking(Long bookingId);

}
