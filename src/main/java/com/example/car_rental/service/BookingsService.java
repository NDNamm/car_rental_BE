package com.example.car_rental.service;

import com.example.car_rental.dto.requestDTO.BookingsRequest.BookingStatusRequest;
import com.example.car_rental.dto.requestDTO.BookingsRequest.BookingRequest;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingsResponse;
import org.springframework.data.domain.Page;

public interface BookingsService {

    //ADMIN
    Page<BookingsResponse> getAllBookings(String bookingCode ,String name, String phoneNumber, String status, int page, int size);
    void updateBookingsStatus(BookingStatusRequest request, Long bookingId);
    void deleteBooking(Long bookingId);

    //USER
    String addBooking(String email, BookingRequest bookingRequest);
    void updateBooking(String email, BookingRequest bookingRequest, Long bookingId);
    Page<BookingsResponse> getAllBookingUser(String email, String bookingCode,String status, int page, int size);
    void cancelBooking(String email,Long bookingId);
    BookingsResponse getBooking(Long bookingId);

}
