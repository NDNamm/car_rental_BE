package com.example.car_rental.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_KEY(9999, "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR),

    PHONE_EXISTED(1009, "Phone already exists", HttpStatus.CONFLICT),
    EMAIL_EXISTED(1011, "Email already exists", HttpStatus.CONFLICT),

    USER_NOT_FOUND(1003, "User not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_EXISTED(1010, "Role not found", HttpStatus.NOT_FOUND),

    INVALID_CREDENTIALS(1012, "Invalid email or password", HttpStatus.UNAUTHORIZED),

    PASSWORD_NOT_MATCH(1002, "Passwords do not match", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1004, "Full name must be at least 5 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1005, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_OLD_NOT_MATCH(1006, "Passwords old do not match", HttpStatus.BAD_REQUEST),
    PHONE_INVALID(1006, "Phone must be at least 10 characters", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(1008, "Email format is invalid", HttpStatus.BAD_REQUEST),

    TOKEN_MISSING(1013, "Authentication token is missing", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID(1014, "Authentication token is invalid", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_INVALID(4003, "REFRESH TOKEN INVALID OR EXPIRED", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(4001, "TOKEN_EXPIRED", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(401, "UNAUTHORIZED", HttpStatus.UNAUTHORIZED),
    INVALID_OTP(4002, "INVALID_OTP", HttpStatus.UNAUTHORIZED),

    VERIFICATION_NOT_FOUND(5001, "Verification not found", HttpStatus.NOT_FOUND),
    ALREADY_VERIFIED(5002, "User already verified", HttpStatus.BAD_REQUEST),
    INVALID_VERIFICATION_STATUS(5003, "Invalid verification status", HttpStatus.BAD_REQUEST),
    CANNOT_UPDATE_NAME_AFTER_VERIFICATION(5004, "Cannot update name after verification", HttpStatus.BAD_REQUEST),

    CATEGORY_EXISTED(6001,"Category already exists", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND(6002,"Category not found", HttpStatus.NOT_FOUND),

    FEATURE_EXISTED(7001,"Feature already exists", HttpStatus.BAD_REQUEST),
    FEATURE_NOT_FOUND(7002,"Feature not found", HttpStatus.NOT_FOUND),

    CAR_EXISTED(8001,"Car already exists", HttpStatus.BAD_REQUEST),
    CAR_NOT_FOUND(8002,"Car not found", HttpStatus.NOT_FOUND),
    IMAGE_DELETE_FAILED(8003,"Failed to delete image", HttpStatus.INTERNAL_SERVER_ERROR),

    UPDATE_IMAGE_FAIL(6003, "Failed to update image", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatus status;
}
