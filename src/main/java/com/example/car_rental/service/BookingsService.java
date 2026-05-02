package com.example.car_rental.service;

import com.example.car_rental.dto.requestDTO.BookingsRequest.BookingStatusRequest;
import com.example.car_rental.dto.requestDTO.BookingsRequest.BookingRequest;
import com.example.car_rental.dto.requestDTO.RefundRequest;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingCodeResponse;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingsResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface BookingsService {

    //ADMIN
//    Page<BookingsResponse> getAllBookings(String bookingCode, String email, String name, String phoneNumber, String status, int page, int size);
    Page<BookingsResponse> getAllBookings(String search, String status, int page, int size);
    void updateBookingsStatus(BookingStatusRequest request, Long bookingId);
    void deleteBooking(Long bookingId);

    //USER
    BookingCodeResponse addBooking(String email, BookingRequest bookingRequest);
    BookingCodeResponse updateBooking(String email, BookingRequest bookingRequest, Long bookingId);
    Page<BookingsResponse> getAllBookingUser(String email, String bookingCode,String status, int page, int size);
    void cancelBooking(String email, Long bookingId, RefundRequest request, MultipartFile qrCode);

}
