package com.example.car_rental.dto.responseDTO.DashboardResponse;

import com.example.car_rental.entity.enums.BookingStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class StatusCountResponse {
    private BookingStatusEnum status;
    private Long count;

    public StatusCountResponse(BookingStatusEnum status, Long count) {
        this.status = status;
        this.count = count;
    }
}
