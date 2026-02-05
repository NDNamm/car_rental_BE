package com.example.car_rental.exception;

import com.example.car_rental.dto.responseDTO.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandle {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<?>> handleAppException(AppException ex) {
        ErrorCode ec = ex.getErrorCode();

        return ResponseEntity.status(ec.getStatus())
                .body(ApiResponse.builder()
                        .code(ec.getCode())
                        .message(ec.getMessage())
                        .build());
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException ex) {
//        return ResponseEntity.badRequest()
//                .body(ApiResponse.builder()
//                        .code(ErrorCode.VALIDATION_ERROR.getCode())
//                        .message(ex.getFieldError().getDefaultMessage())
//                        .build());
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiResponse<?>> handleSystem(Exception ex) {
//        log.error("System error", ex);
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(ApiResponse.builder()
//                        .code(ErrorCode.SYSTEM_ERROR.getCode())
//                        .message(ErrorCode.SYSTEM_ERROR.getMessage())
//                        .build());
//    }
}
