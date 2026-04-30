package com.example.car_rental.dto.requestDTO.paymentsRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SePayWebhookRequest {

    private Long id;
    private String gateway;
    private String transactionDate; // Ngày giờ giao dịch
    private String accountNo;     // Số tài khoản nhận tiền
    private BigDecimal transferAmount;    // Số tiền khách chuyển
    private String content;       // Nội dung chuyển khoản (QUAN TRỌNG: Chứa mã BK_...)
    private String transferType;  // Loại giao dịch (In/Out)
    private String referenceCode; // Mã tham chiếu của ngân hàng (Transaction ID)
}