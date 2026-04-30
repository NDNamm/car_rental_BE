package com.example.car_rental.controller;

import com.example.car_rental.dto.requestDTO.BookingsRequest.BookingStatusRequest;
import com.example.car_rental.dto.requestDTO.BookingsRequest.BookingRequest;
import com.example.car_rental.dto.requestDTO.CarRequest.CarRequest;
import com.example.car_rental.dto.requestDTO.RefundRequest;
import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingCodeResponse;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingsResponse;
import com.example.car_rental.dto.responseDTO.RefundResponse;
import com.example.car_rental.service.BookingsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {

    final private BookingsService bookingsService;

    @GetMapping("/1")
    public String geta() {
        return "NAM";
    }
    //ADMIN
    @GetMapping("")
    public ApiResponse<Page<BookingsResponse>> getAllBookings(@RequestParam(required = false) String bookingCode,
                                                              @RequestParam(required = false) String email,
                                                              @RequestParam(required = false) String name,
                                                              @RequestParam(required = false) String phone,
                                                              @RequestParam(required = false) String status,
                                                              @RequestParam(defaultValue = "0", required = false) int page,
                                                              @RequestParam(defaultValue = "5") int size) {

        return ApiResponse.<Page<BookingsResponse>>builder()
                .code(200)
                .data(bookingsService.getAllBookings(bookingCode,email, name, phone, status, page, size))
                .build();
    }

    @PatchMapping("/{bookingId}")
    public ApiResponse<Void> updateStatus(@PathVariable(value = "bookingId") Long bookingId,
                                          @RequestBody(required = false) BookingStatusRequest request) {

        bookingsService.updateBookingsStatus(request, bookingId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Update status booking successful")
                .build();
    }

    @DeleteMapping("/{bookingId}")
    public ApiResponse<Void> deleteBooking(@PathVariable("bookingId") Long bookingId) {

        bookingsService.deleteBooking(bookingId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete booking successful")
                .build();
    }


    //USER
    @GetMapping("/me")
    public ApiResponse<Page<BookingsResponse>> getAllBookingsUser(@RequestParam(required = false) String bookingCode,
                                                                  @RequestParam(required = false) String status,
                                                                  @RequestParam(defaultValue = "0", required = false) int page,
                                                                  @RequestParam(defaultValue = "5") int size) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return ApiResponse.<Page<BookingsResponse>>builder()
                .code(200)
                .data(bookingsService.getAllBookingUser(email, bookingCode, status, page, size))
                .build();
    }

    @PostMapping("/me")
    public ApiResponse<BookingCodeResponse> addBooking(@RequestBody BookingRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return ApiResponse.<BookingCodeResponse>builder()
                .code(200)
                .data(bookingsService.addBooking(email, request))
                .message("Add booking successful")
                .build();
    }

    @PutMapping("/me/{bookingId}")
    public ApiResponse<BookingCodeResponse> updateBooking(@PathVariable(value = "bookingId") Long bookingId ,
                                           @RequestBody BookingRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ApiResponse.<BookingCodeResponse>builder()
                .code(200)
                .message("Update booking successful")
                .data(bookingsService.updateBooking(email,request, bookingId ))
                .build();
    }

    @PatchMapping("/me/{bookingId}")
    public ApiResponse<Void> cancelBooking(@PathVariable(value = "bookingId") Long bookingId,
                                                     @RequestPart(value = "refundRequest", required = false) String request,
                                                     @RequestPart(required = false) MultipartFile qrCode) throws JsonProcessingException {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ObjectMapper objectMapper = new ObjectMapper();
        RefundRequest refundRequest = objectMapper.readValue(request, RefundRequest.class);

        bookingsService.cancelBooking(email, bookingId, refundRequest, qrCode );
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Cancel booking successful")
                .build();
    }

}
