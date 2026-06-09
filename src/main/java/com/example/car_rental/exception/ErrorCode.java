package com.example.car_rental.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_KEY(9999, "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR),
    SYSTEM_USER_NOT_FOUND(1000, "System user not found", HttpStatus.NOT_FOUND),

    PASSWORD_NOT_MATCH(1001, "Passwords do not match", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1002, "User not found", HttpStatus.NOT_FOUND),
    USERNAME_INVALID(1003, "Full name must be at least 5 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_OLD_NOT_MATCH(1005, "Passwords old do not match", HttpStatus.BAD_REQUEST),
    PHONE_INVALID(1006, "Phone must be at least 10 characters", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1007, "Email format is invalid", HttpStatus.BAD_REQUEST),
    PHONE_EXISTED(1008, "Phone already exists", HttpStatus.CONFLICT),
    ROLE_NOT_FOUND(1009, "Role not found", HttpStatus.NOT_FOUND),
    EMAIL_EXISTED(1010, "Email already exists", HttpStatus.CONFLICT),
    INVALID_CREDENTIALS(1011, "Invalid email or password", HttpStatus.UNAUTHORIZED),
    TOKEN_MISSING(1012, "Authentication token is missing", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID(1013, "Authentication token is invalid", HttpStatus.UNAUTHORIZED),

    UNAUTHORIZED(4001, "UNAUTHORIZED", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(4002, "TOKEN_EXPIRED", HttpStatus.UNAUTHORIZED),
    INVALID_OTP(4003, "INVALID_OTP", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_INVALID(4004, "REFRESH TOKEN INVALID OR EXPIRED", HttpStatus.UNAUTHORIZED),

    VERIFICATION_NOT_FOUND(5001, "Verification not found", HttpStatus.NOT_FOUND),
    ALREADY_VERIFIED(5002, "User already verified", HttpStatus.BAD_REQUEST),
    INVALID_VERIFICATION_STATUS(5003, "Invalid verification status", HttpStatus.BAD_REQUEST),
    CANNOT_UPDATE_NAME_AFTER_VERIFICATION(5004, "Cannot update name after verification", HttpStatus.BAD_REQUEST),
    USER_NOT_VERIFIED(5005, "USER_NOT_VERIFIED", HttpStatus.BAD_REQUEST),
    VERIFICATION_PENDING_OR_REJECTED(5006, "VERIFICATION_PENDING_OR_REJECTED", HttpStatus.BAD_REQUEST),

    CATEGORY_EXISTED(6001, "Category already exists", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND(6002, "Category not found", HttpStatus.NOT_FOUND),
    UPDATE_IMAGE_FAIL(6003, "Failed to update image", HttpStatus.INTERNAL_SERVER_ERROR),
    IMAGE_REQUIRED(6004, "Image is required", HttpStatus.BAD_REQUEST),

    FEATURE_EXISTED(7001, "Feature already exists", HttpStatus.BAD_REQUEST),
    FEATURE_NOT_FOUND(7002, "Feature not found", HttpStatus.NOT_FOUND),

    CAR_EXISTED(8001, "Car already exists", HttpStatus.BAD_REQUEST),
    CAR_NOT_FOUND(8002, "Car not found", HttpStatus.NOT_FOUND),
    IMAGE_DELETE_FAILED(8003, "Failed to delete image", HttpStatus.INTERNAL_SERVER_ERROR),
    CAR_NOT_AVAILABLE(8004, "Car not AVAILABLE", HttpStatus.BAD_REQUEST),
    INVALID_DATE_RANGE(8005, "INVALID_DATE_RANGE", HttpStatus.BAD_REQUEST),
    CAR_ALREADY_BOOKED(8006, "CAR_ALREADY_BOOKED", HttpStatus.BAD_REQUEST),
    INVALID_CAR_STATUS(8007, "INVALID_CAR_STATUS", HttpStatus.BAD_REQUEST),

    BOOKING_NOT_FOUND(9001, "Booking not found", HttpStatus.NOT_FOUND),
    CANNOT_UPDATE_BOOKING(9002, "CANNOT_UPDATE_BOOKING", HttpStatus.BAD_REQUEST),
    BOOKING_ALREADY_CLOSED(9003, "Booking already closed", HttpStatus.BAD_REQUEST),
    CANNOT_CANCEL_BOOKING(9004, "CANNOT_CANCEL_BOOKING", HttpStatus.BAD_REQUEST),
    INVALID_STATUS_TRANSITION(9005, "Invalid status transition", HttpStatus.BAD_REQUEST),
    CAR_ALREADY_BOOKED_IN_THIS_PERIOD(9006, "CAR_ALREADY_BOOKED_IN_THIS_PERIOD", HttpStatus.BAD_REQUEST),
    BOOKING_ALREADY_PROCESSED(9007, "BOOKING_ALREADY_PROCESSED", HttpStatus.BAD_REQUEST),
    CAR_ALREADY_PICKED_UP_CANNOT_CANCEL(9008, "CAR_ALREADY_PICKED_UP_CANNOT_CANCEL", HttpStatus.BAD_REQUEST),
    HANDOVER_NOT_FOUND(9009, "HANDOVER_NOT_FOUND", HttpStatus.NOT_FOUND),
    START_DATE_IN_PAST(9010, "START_DATE_IN_PAST", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST(9011, "INVALID_REQUEST", HttpStatus.BAD_REQUEST),
    CAR_ALREADY_BOOKED_IN_THIS_TIME(9012, "CAR_ALREADY_BOOKED_IN_THIS_TIME", HttpStatus.BAD_REQUEST),
    QR_CODE_REQUIRED(9013, "QR_CODE_REQUIRED", HttpStatus.BAD_REQUEST),
    BOOKING_NOT_COMPLETED(9014, "Booking not completed", HttpStatus.BAD_REQUEST),
    INVALID_BOOKING_TIME(9015, "Invalid booking time", HttpStatus.BAD_REQUEST),
    INVALID_BOOKING_STATUS(9016, "Invalid booking status for refund", HttpStatus.BAD_REQUEST),
    DETAIL_NOT_FOUND(9017, "Booking detail not found", HttpStatus.NOT_FOUND),
    BOOKING_DETAIL_NOT_FOUND(9018, "Booking detail not found", HttpStatus.NOT_FOUND),

    INCIDENT_NOT_FOUND(10001, "Incident not found", HttpStatus.NOT_FOUND),
    PENALTY_NOT_FOUND(11001, "Penalty not found", HttpStatus.NOT_FOUND),
    PENALTY_RULES_NOT_FOUND(11002, "Penalty rules not found", HttpStatus.NOT_FOUND),

    UNAUTHORIZED_WEBHOOK(12001, "UNAUTHORIZED_WEBHOOK", HttpStatus.UNAUTHORIZED),
    PAYMENT_NOT_FOUND(12002, "PAYMENT_NOT_FOUND", HttpStatus.NOT_FOUND),
    PAYMENT_NOT_COMPLETED(12003, "Payment not completed", HttpStatus.BAD_REQUEST),

    REFUND_ALREADY_EXISTS(13001, "Refund already exists for this booking", HttpStatus.BAD_REQUEST),
    REFUND_NOT_FOUND(13002, "REFUND not found", HttpStatus.NOT_FOUND),
    REFUND_ALREADY_COMPLETED(13003, "REFUND already completed", HttpStatus.BAD_REQUEST),

    REVIEW_ALREADY_EXISTS(14001, "Review already exists for this booking", HttpStatus.BAD_REQUEST),
    REVIEW_NOT_FOUND(14002, "Review not found", HttpStatus.NOT_FOUND),
    REVIEW_ALREADY_APPROVED(14003, "Review already approved", HttpStatus.BAD_REQUEST),
    INVALID_STATUS(14004, "Status invalid", HttpStatus.BAD_REQUEST),

    INVOICE_NOT_FOUND(15001, "Invoice not found", HttpStatus.NOT_FOUND),

    NOTIFICATION_NOT_FOUND(16001, "Notification not found", HttpStatus.NOT_FOUND);

    private final int code;
    private final String message;
    private final HttpStatus status;
}