package com.example.car_rental.dto.requestDTO.BookingsRequest;

import com.example.car_rental.entity.enums.BookingStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingStatusRequest {

    @NotNull(message = "Trạng thái mới không được để trống")
    private BookingStatusEnum status;

    private String reason;

    private String note;
}
