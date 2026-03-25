package com.example.car_rental.dto.requestDTO.paymentsRequest;

import com.example.car_rental.entity.enums.PaymentMethodEnum;
import com.example.car_rental.entity.enums.PaymentStatusEnum;
import com.example.car_rental.entity.enums.PaymentTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentsRequest {

    private BigDecimal amount;
    private PaymentMethodEnum paymentMethodEnum;
    private PaymentTypeEnum paymentTypeEnum;
    private PaymentStatusEnum status;
    private String transactionCode;
    private LocalDate createdAt;
}
