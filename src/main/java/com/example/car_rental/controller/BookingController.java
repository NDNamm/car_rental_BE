package com.example.car_rental.controller;

import com.example.car_rental.dto.requestDTO.BookingsRequest.BookingStatusRequest;
import com.example.car_rental.dto.requestDTO.BookingsRequest.BookingRequest;
import com.example.car_rental.dto.requestDTO.BookingsRequest.ReplaceCarRequest;
import com.example.car_rental.dto.requestDTO.CarRequest.CarRequest;
import com.example.car_rental.dto.requestDTO.RefundRequest;
import com.example.car_rental.dto.requestDTO.handoverRequest.HandoverRequest;
import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingCodeResponse;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingsResponse;
import com.example.car_rental.dto.responseDTO.CarResponse.CarResponse;
import com.example.car_rental.dto.responseDTO.RefundResponse;
import com.example.car_rental.service.BookingsService;
import com.example.car_rental.service.CarService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {

    final private BookingsService bookingsService;
    private final CarService carService;

    //ADMIN
    @GetMapping("")
    public ApiResponse<Page<BookingsResponse>> getAllBookings(@RequestParam(required = false) String search,
                                                              @RequestParam(required = false) String status,
                                                              @RequestParam(defaultValue = "0", required = false) int page,
                                                              @RequestParam(defaultValue = "5") int size) {

        return ApiResponse.<Page<BookingsResponse>>builder()
                .code(200)
                .data(bookingsService.getAllBookings(search, status, page, size))
                .build();
    }

    @GetMapping("/{bookingId}")
    public ApiResponse<BookingsResponse> getBookingById(@PathVariable Long bookingId) {

        return ApiResponse.<BookingsResponse>builder()
                .code(200)
                .data(bookingsService.getBookingById(bookingId))
                .build();
    }

    @PatchMapping("/{bookingId}")
    public ApiResponse<Void> updateStatus(@PathVariable(value = "bookingId") Long bookingId,
                                          @RequestPart(value = "bookingRequest", required = false) String request,
                                          @RequestPart(value = "handoverRequest", required = false) String handover,
                                          @RequestPart(required = false) MultipartFile[] images) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        HandoverRequest handoverRequest = new HandoverRequest();
        if (handover != null && !handover.isBlank()) {
            handoverRequest = objectMapper.readValue(handover, HandoverRequest.class);
        }

        BookingStatusRequest bookingRequest = new BookingStatusRequest();
        if (request != null && !request.isBlank()) {
            bookingRequest = objectMapper.readValue(request, BookingStatusRequest.class);
        }

        bookingsService.updateBookingsStatus(bookingRequest, bookingId, handoverRequest, images);
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

    @GetMapping("/{bookingId}/available-cars")
    public ApiResponse<List<CarResponse>> getAvailableCars(@PathVariable(value = "bookingId") Long bookingId) {

        return ApiResponse.<List<CarResponse>>builder()
                .code(200)
                .data(carService.getAvailableReplacementCars(bookingId))
                .build();
    }

    @PutMapping("/{bookingId}/replace-car")
    public ApiResponse<Void> replaceCar(@PathVariable Long bookingId,
                                        @RequestBody ReplaceCarRequest request) {

        bookingsService.replaceCar(bookingId, request);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Replace car success")
                .build();
    }


    //USER
    @GetMapping("/me")
    public ApiResponse<Page<BookingsResponse>> getAllBookingsUser(@RequestParam(required = false) String bookingCode,
                                                                  @RequestParam(required = false) String status,
                                                                  @RequestParam(defaultValue = "0", required = false) int page,
                                                                  @RequestParam(defaultValue = "5") int size) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("EMAIL AUTH: " + email);
        log.info("EMAIL AUTH: " + email);
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        return ApiResponse.<Page<BookingsResponse>>builder()
                .code(200)
                .data(bookingsService.getAllBookingUser(email, bookingCode, status, page, size))
                .build();
    }

    @GetMapping("/me/{bookingId}")
    public ApiResponse<BookingsResponse> getBookingUser(@PathVariable Long bookingId) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ApiResponse.<BookingsResponse>builder()
                .code(200)
                .data(bookingsService.getBookingUserById(email, bookingId))
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
    public ApiResponse<BookingCodeResponse> updateBooking(@PathVariable(value = "bookingId") Long bookingId,
                                                          @RequestBody BookingRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ApiResponse.<BookingCodeResponse>builder()
                .code(200)
                .message("Update booking successful")
                .data(bookingsService.updateBooking(email, request, bookingId))
                .build();
    }

    @PatchMapping(value = "/me/{bookingId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Void> cancelBooking(@PathVariable(value = "bookingId") Long bookingId,
                                           @RequestPart(value = "refundRequest", required = false) String request,
                                           @RequestPart(required = false) MultipartFile qrCode) throws JsonProcessingException {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        ObjectMapper objectMapper = new ObjectMapper();
        RefundRequest refundRequest = new RefundRequest();
        if (request != null && !request.isBlank()) {
            refundRequest = objectMapper.readValue(request, RefundRequest.class);
        }

        bookingsService.cancelBooking(email, bookingId, refundRequest, qrCode);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Cancel booking successful")
                .build();
    }

}
