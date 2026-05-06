package com.example.car_rental.dto.requestDTO.ReviewRequest;

import com.example.car_rental.entity.enums.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateReviewRequest {
    private ReviewStatus status;
    private String reason;
}
