package com.example.car_rental.controller;

import com.example.car_rental.dto.requestDTO.BookingsRequest.BookingRequest;
import com.example.car_rental.dto.requestDTO.BookingsRequest.BookingStatusRequest;
import com.example.car_rental.dto.requestDTO.RefundRequest;
import com.example.car_rental.dto.requestDTO.ReviewRequest.ReviewRequest;
import com.example.car_rental.dto.requestDTO.ReviewRequest.UpdateReviewRequest;
import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingCodeResponse;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingsResponse;
import com.example.car_rental.dto.responseDTO.ReviewResponse;
import com.example.car_rental.entity.Reviews;
import com.example.car_rental.service.BookingsService;
import com.example.car_rental.service.ReviewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    final private ReviewService reviewService;

    //ADMIN
    @GetMapping("")
    public ApiResponse<Page<ReviewResponse>> getAllReviews(@RequestParam(required = false) String status,
                                                           @RequestParam(defaultValue = "0", required = false) int page,
                                                           @RequestParam(defaultValue = "5") int size) {

        return ApiResponse.<Page<ReviewResponse>>builder()
                .code(200)
                .data(reviewService.getAllReviews(status, page, size))
                .build();
    }

    @PutMapping("/{reviewId}")
    public ApiResponse<Void> updateStatus(@PathVariable(value = "reviewId") Long reviewId,
                                          @RequestBody(required = false) UpdateReviewRequest request) {

        reviewService.updateStatusReview(reviewId, request);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Update status review successful")
                .build();
    }


    //USER
    @GetMapping("/me")
    public ApiResponse<Page<ReviewResponse>> getAllReviewUser(@RequestParam(required = false) String status,
                                                                @RequestParam(defaultValue = "0", required = false) int page,
                                                                @RequestParam(defaultValue = "5") int size) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return ApiResponse.<Page<ReviewResponse>>builder()
                .code(200)
                .data(reviewService.getAllReviewUser(email, status, page, size))
                .build();
    }

    @GetMapping("/car/{carId}")
    public ApiResponse<List<ReviewResponse>> getAllReviewsByCar(@PathVariable(value = "carId") Long carId) {

        return ApiResponse.<List<ReviewResponse>>builder()
                .code(200)
                .data(reviewService.getReviewsByCarId(carId))
                .build();
    }

    @PostMapping("/me/{bookingId}")
    public ApiResponse<ReviewResponse> createReview(@PathVariable(value = "bookingId") Long bookingId,
                                                    @RequestBody ReviewRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return ApiResponse.<ReviewResponse>builder()
                .code(200)
                .data(reviewService.createReview(email,bookingId, request))
                .message("Add Review successful")
                .build();
    }

    @PutMapping("/me/{reviewId}")
    public ApiResponse<ReviewResponse> updateBooking(@PathVariable(value = "reviewId") Long reviewId,
                                                     @RequestBody ReviewRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ApiResponse.<ReviewResponse>builder()
                .code(200)
                .message("Update Review successful")
                .data(reviewService.updateReview(email,reviewId, request))
                .build();
    }

}
