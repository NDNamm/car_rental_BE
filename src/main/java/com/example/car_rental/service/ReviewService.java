package com.example.car_rental.service;

import com.example.car_rental.dto.requestDTO.ReviewRequest.ReviewRequest;
import com.example.car_rental.dto.requestDTO.ReviewRequest.UpdateReviewRequest;
import com.example.car_rental.dto.responseDTO.ReviewResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReviewService {
    Page<ReviewResponse> getAllReviewUser(String email, Boolean is_hidden, int page, int size);
    ReviewResponse createReview(String email, Long bookingId, ReviewRequest request);
    ReviewResponse updateReview(String email, Long reviewId, ReviewRequest request);
    List<ReviewResponse> getReviewsByCarId(Long carId);
//    void deleteReview(Long categoryId);

    //ADMIN
    Page<ReviewResponse> getAllReviews(Boolean is_hidden, int page, int size);
    void updateStatusReview(Long reviewId, UpdateReviewRequest request);

}
