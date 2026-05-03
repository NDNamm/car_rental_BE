package com.example.car_rental.service;

import com.example.car_rental.dto.requestDTO.ReviewRequest.ReviewRequest;
import com.example.car_rental.dto.requestDTO.ReviewRequest.UpdateReviewRequest;
import com.example.car_rental.dto.responseDTO.ReviewResponse;
import org.springframework.data.domain.Page;

public interface ReviewService {
    Page<ReviewResponse> getAllReviewUser(String name, int page, int size);
    ReviewResponse createReview(String email, Long bookingId, ReviewRequest request);
    ReviewResponse updateReview(String email, Long reviewId, ReviewRequest request);
//    void deleteReview(Long categoryId);
    Page<ReviewResponse> getAllReviews(String name, int page, int size);
    void updateStatusReview(Long reviewId, UpdateReviewRequest request);

}
