package com.example.car_rental.dto.requestDTO.BookingsRequest;

import com.example.car_rental.entity.enums.BookingStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingStatusRequest {

    @NotNull(message = "Trạng thái mới không được để trống")
    private BookingStatusEnum status;
    private String reason;
    private String note;
}
