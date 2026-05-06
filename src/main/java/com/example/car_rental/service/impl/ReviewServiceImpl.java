package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.CategoryRequest;
import com.example.car_rental.dto.requestDTO.ReviewRequest.ReviewRequest;
import com.example.car_rental.dto.requestDTO.ReviewRequest.UpdateReviewRequest;
import com.example.car_rental.dto.responseDTO.CategoryResponse;
import com.example.car_rental.dto.responseDTO.ReviewResponse;
import com.example.car_rental.entity.*;
import com.example.car_rental.entity.enums.BookingStatusEnum;
import com.example.car_rental.entity.enums.ReviewStatus;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.*;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    final BookingsRepository bookingRepository;
    final ReviewRepository reviewRepository;
    final UserRepository userRepository;
    final CarRepository carRepository;

    @Override
    public Page<ReviewResponse> getAllReviewUser(String email, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reviews> reviewsPage = null;
        boolean noFilter = (status == null || status.isBlank());

        if (noFilter) {
            reviewsPage = reviewRepository.findReviewByUser(email, null, pageable);
        } else {
            ReviewStatus statusEnum = null;

            if (!status.isBlank()) {
                statusEnum = ReviewStatus.valueOf(status.toUpperCase());
            }
            reviewsPage = reviewRepository.findReviewByUser(email, statusEnum, pageable);
        }

        return reviewsPage.map(this::mapToReviewResponse);
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

        if (!booking.getStatus().equals(BookingStatusEnum.COMPLETED)) {
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
                .status(ReviewStatus.PENDING)
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

        if (review.getStatus().equals(ReviewStatus.APPROVED)) {
            throw new AppException(ErrorCode.REVIEW_ALREADY_APPROVED);
        }

        if (!review.getUser().getEmail().equals(email)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setStatus(ReviewStatus.PENDING);
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);

        return mapToReviewResponse(review);
    }

    @Override
    public List<ReviewResponse> getReviewsByCarId(Long carId) {
        return reviewRepository.findApprovedReviewsByCarId(carId, ReviewStatus.APPROVED)
                .stream()
                .map(this::mapToReviewResponse)
                .toList();
    }

    @Override
    public Page<ReviewResponse> getAllReviews(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reviews> reviewsPage = null;
        boolean noFilter = (status == null || status.isBlank());

        if (noFilter) {
            reviewsPage = reviewRepository.findAll(pageable);
        } else {
            ReviewStatus statusEnum = null;

            if (!status.isBlank()) {
                statusEnum = ReviewStatus.valueOf(status.toUpperCase());
            }
            reviewsPage = reviewRepository.findAllReview(statusEnum, pageable);
        }

        return reviewsPage.map(this::mapToReviewResponse);
    }

    @Override
    public void updateStatusReview(Long reviewId, UpdateReviewRequest request) {
        Reviews review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

        ReviewStatus status = request.getStatus();

        if (status == ReviewStatus.APPROVED) {
            review.setStatus(ReviewStatus.APPROVED);
            review.setReason(null);

            updateCarRating(review.getCar().getId());
        }
        else if (status == ReviewStatus.REJECTED) {
            review.setStatus(ReviewStatus.REJECTED);
            review.setReason(request.getReason());
        }
        else {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }
        reviewRepository.save(review);
    }

    private ReviewResponse mapToReviewResponse(Reviews review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .fullName(review.getUser().getFullName())
                .email(review.getUser().getEmail())
                .carId(review.getCar().getId())
                .carName(review.getCar().getName())
                .status(review.getStatus())
                .reason(review.getReason())
                .createdAt(review.getCreatedAt())
                .build();
    }

    private void updateCarRating(Long carId) {
        Object[] result = (Object[]) reviewRepository.getRatingStats(carId);

        Double avg = result[0] != null
                ? new java.math.BigDecimal(result[0].toString()).doubleValue()
                : 0.0;
        Long count = result[1] != null ? ((Number) result[1]).longValue() : 0L;

        Cars car = carRepository.findById(carId)
                .orElseThrow(() -> new AppException(ErrorCode.CAR_NOT_FOUND));

        car.setAverageRating(avg);
        car.setTotalReviews(count);

        carRepository.save(car);
    }
}
