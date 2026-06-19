package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.ReviewRequest.ReviewRequest;
import com.example.car_rental.dto.requestDTO.ReviewRequest.UpdateReviewRequest;
import com.example.car_rental.dto.responseDTO.ReviewResponse;
import com.example.car_rental.entity.*;
import com.example.car_rental.entity.enums.BookingStatusEnum;
import com.example.car_rental.entity.enums.NotificationTypeEnum;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.*;
import com.example.car_rental.service.NotificationService;
import com.example.car_rental.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    final BookingsRepository bookingRepository;
    final ReviewRepository reviewRepository;
    final UserRepository userRepository;
    final CarRepository carRepository;
    final private NotificationService notificationService;

    @Override
    public Page<ReviewResponse> getAllReviewUser(String email, Boolean isHidden, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Reviews> reviewsPage = reviewRepository.findReviewByUser(email, isHidden, pageable);

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
                .car(booking.getBookingDetailsList().get(0).getCar())
                .isHidden(false)
                .createdAt(LocalDateTime.now())
                .build();

        reviewRepository.save(review);

        updateCarRating(review.getCar().getId());
        createReviewNotification(user, review.getId(), booking.getBookingCode());
        return mapToReviewResponse(review);
    }

    @Override
    public ReviewResponse updateReview(String email, Long reviewId, ReviewRequest request) {

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Reviews review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

        if (!review.getUser().getEmail().equals(email)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());
        reviewRepository.save(review);

        updateCarRating(review.getCar().getId());

        return mapToReviewResponse(review);
    }

    @Override
    public List<ReviewResponse> getReviewsByCarId(Long carId) {

        return reviewRepository.findVisibleReviewsByCarId(carId)
                .stream()
                .map(this::mapToReviewResponse)
                .toList();
    }

    @Override
    public Page<ReviewResponse> getAllReviews(Boolean isHidden, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Reviews> reviewsPage;
        if (isHidden == null) {
            reviewsPage = reviewRepository.findAll(pageable);
        } else {
            reviewsPage = reviewRepository.findAllReview(isHidden, pageable);
        }

        return reviewsPage.map(this::mapToReviewResponse);
    }

    @Override
    public void updateStatusReview(Long reviewId, UpdateReviewRequest request) {

        Reviews review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));

        Boolean isHidden = request.getIsHidden();

        review.setIsHidden(isHidden);

        if (Boolean.TRUE.equals(isHidden)) {
            review.setReason(request.getReason());
            createReviewHiddenNotification(review.getUser(), reviewId);
        } else {
            review.setReason(null);
        }

        reviewRepository.save(review);

        updateCarRating(review.getCar().getId());
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
                .isHidden(review.getIsHidden())
                .reason(review.getReason())
                .createdAt(review.getCreatedAt())
                .bookingCode(review.getBooking().getBookingCode())
                .carImage(review.getCar().getCarImages().get(0).getImageUrl())
                .build();
    }

    private void updateCarRating(Long carId) {

        Object[] result = (Object[]) reviewRepository.getRatingStats(carId);

        Double avg = result[0] != null ? new java.math.BigDecimal(result[0].toString()).doubleValue() : 0.0;

        Long count = result[1] != null ? ((Number) result[1]).longValue() : 0L;

        Cars car = carRepository.findById(carId)
                .orElseThrow(() -> new AppException(ErrorCode.CAR_NOT_FOUND));

        car.setAverageRating(avg);
        car.setTotalReviews(count);

        carRepository.save(car);
    }

    private void createReviewNotification(Users user, Long reviewId, String bookingCode) {
        String message = "Bạn đã gửi đánh giá thành công cho mã đặt xe " + bookingCode
                + ". Cảm ơn bạn đã sử dụng dịch vụ!";
        notificationService.createNotification(
                user.getId(),
                NotificationTypeEnum.REVIEW,
                message,
                reviewId);
    }

    private void createReviewHiddenNotification(Users user, Long reviewId) {
        String message = "Đánh giá của bạn đã bị ẩn do vi phạm chính sách. Vui lòng kiểm tra lại nội dung.";
        notificationService.createNotification(
                user.getId(),
                NotificationTypeEnum.REVIEW,
                message,
                reviewId);
    }
}