package com.example.car_rental.dto.responseDTO.DashboardResponse;

import com.example.car_rental.entity.enums.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class StatusCountResponse {
    private BookingStatusEnum status;
    private Long count;
    private CarStatusEnum carStatus;
    private InvoiceStatusEnum invoiceStatus;

    public StatusCountResponse(BookingStatusEnum status, Long count) {
        this.status = status;
        this.count = count;
    }

    public StatusCountResponse(CarStatusEnum carStatus, Long count) {
        this.carStatus = carStatus;
        this.count = count;
    }

    public StatusCountResponse(InvoiceStatusEnum invoiceStatus, Long count) {
        this.invoiceStatus = invoiceStatus;
        this.count = count;
    }

}
