package com.example.car_rental.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewResponse {
    private Long id;
    private String comment;
    private Long carId;
    private String carName;
    private String fullName;
    private Integer rating;
    private LocalDateTime createdAt;
    private Boolean isHidden;
    private String reason;
    private String email;
    private String bookingCode;
    private String carImage;
}
