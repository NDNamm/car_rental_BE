package com.example.car_rental.dto.requestDTO.ReviewRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {
    private Integer rating;
    private String comment;
    private Long carId;
}
