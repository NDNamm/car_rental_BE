package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.CategoryRequest;
import com.example.car_rental.dto.requestDTO.ReviewRequest.ReviewRequest;
import com.example.car_rental.dto.requestDTO.ReviewRequest.UpdateReviewRequest;
import com.example.car_rental.dto.responseDTO.CategoryResponse;
import com.example.car_rental.dto.responseDTO.ReviewResponse;
import com.example.car_rental.entity.Bookings;
import com.example.car_rental.entity.Categories;
import com.example.car_rental.entity.Reviews;
import com.example.car_rental.entity.Users;
import com.example.car_rental.entity.enums.BookingStatusEnum;
import com.example.car_rental.entity.enums.ReviewStatus;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.BookingsRepository;
import com.example.car_rental.repository.CategoryRepository;
import com.example.car_rental.repository.ReviewRepository;
import com.example.car_rental.repository.UserRepository;
import com.example.car_rental.service.CategoryService;
import com.example.car_rental.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    final BookingsRepository bookingRepository;
    final ReviewRepository reviewRepository;
    final UserRepository userRepository;

    @Override
    public Page<ReviewResponse> getAllReviewUser(String name, int page, int size) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            return reviewRepository.findByUserEmail(name, pageable)
                    .map(this::mapToReviewResponse);
        return null;
    }

    @Override
    public ReviewResponse createReview(String email, Long bookingId, ReviewRequest request) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Bookings booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        if (!booking.getUser().getEmail().equals(email)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (!booking.getStatus().equals(BookingStatusEnum.CONFIRMED)) {
            throw new AppException(ErrorCode.BOOKING_NOT_COMPLETED);
        }

        if (reviewRepository.existsByBookingId(bookingId)) {
            throw new AppException(ErrorCode.REVIEW_ALREADY_EXISTS);
        }
        Reviews review = Reviews.builder()
                .rating(request.getRating())
                .comment(request.getComment())
                .user(user)
                .booking(booking)
                .createdAt(LocalDateTime.now())
                .car(booking.getBookingDetailsList().get(0).getCar())
                .build();

        reviewRepository.save(review);

        return mapToReviewResponse(review);
    }

    @Override
    public ReviewResponse updateReview(String email, Long reviewId, ReviewRequest request) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Reviews review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

        if(review.getStatus().equals(ReviewStatus.APPROVED)){
            throw new AppException(ErrorCode.REVIEW_ALREADY_APPROVED);
        }

        if (!review.getUser().getEmail().equals(email)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        // nếu có duyệt lại
        review.setStatus(ReviewStatus.PENDING);
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);

        return mapToReviewResponse(review);
    }

    @Override
    public Page<ReviewResponse> getAllReviews(String name, int page, int size) {
        return null;
    }

    @Override
    public void updateStatusReview(Long reviewId, UpdateReviewRequest request) {

    }

    private ReviewResponse mapToReviewResponse(Reviews review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .userFullName(review.getUser().getFullName())
                .carId(review.getCar().getId())
                .carName(review.getCar().getName())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
