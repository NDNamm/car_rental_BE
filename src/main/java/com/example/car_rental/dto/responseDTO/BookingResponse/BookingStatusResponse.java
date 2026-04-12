package com.example.car_rental.dto.responseDTO.BookingResponse;

import com.example.car_rental.entity.enums.BookingStatusEnum;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingStatusResponse {

    private Long id;
    private BookingStatusEnum oldStatus;
    private BookingStatusEnum newStatus;
    private String reason;
    private String note;
    private String changedBy;
    private LocalDateTime changedAt;
}
