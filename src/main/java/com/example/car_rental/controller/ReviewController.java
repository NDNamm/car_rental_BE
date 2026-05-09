package com.example.car_rental.controller;

import com.example.car_rental.dto.requestDTO.ReviewRequest.ReviewRequest;
import com.example.car_rental.dto.requestDTO.ReviewRequest.UpdateReviewRequest;
import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.ReviewResponse;
import com.example.car_rental.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("")
    public ApiResponse<Page<ReviewResponse>> getAllReviews(@RequestParam(required = false) Boolean isHidden,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "5") int size
    ) {

        return ApiResponse.<Page<ReviewResponse>>builder()
                .code(200)
                .data(reviewService.getAllReviews(isHidden, page, size))
                .build();
    }

    @PutMapping("/{reviewId}")
    public ApiResponse<Void> updateStatus(@PathVariable Long reviewId,
                                          @RequestBody UpdateReviewRequest request
    ) {

        reviewService.updateStatusReview(reviewId, request);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Update review successful")
                .build();
    }


    @GetMapping("/me")
    public ApiResponse<Page<ReviewResponse>> getAllReviewUser(@RequestParam(required = false) Boolean isHidden,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "5") int size
    ) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return ApiResponse.<Page<ReviewResponse>>builder()
                .code(200)
                .data(reviewService.getAllReviewUser(email, isHidden, page, size))
                .build();
    }

    @GetMapping("/car/{carId}")
    public ApiResponse<List<ReviewResponse>> getAllReviewsByCar(@PathVariable Long carId
    ) {

        return ApiResponse.<List<ReviewResponse>>builder()
                .code(200)
                .data(reviewService.getReviewsByCarId(carId))
                .build();
    }

    @PostMapping("/me/{bookingId}")
    public ApiResponse<ReviewResponse> createReview(@PathVariable Long bookingId,
                                                    @RequestBody ReviewRequest request
    ) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ApiResponse.<ReviewResponse>builder()
                .code(200)
                .message("Add review successful")
                .data(reviewService.createReview(email, bookingId, request))
                .build();
    }

    @PutMapping("/me/{reviewId}")
    public ApiResponse<ReviewResponse> updateReview(@PathVariable Long reviewId,
                                                    @RequestBody ReviewRequest request
    ) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return ApiResponse.<ReviewResponse>builder()
                .code(200)
                .message("Update review successful")
                .data(reviewService.updateReview(email, reviewId, request))
                .build();
    }
}