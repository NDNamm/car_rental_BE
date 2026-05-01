package com.example.car_rental.domain;

import com.example.car_rental.entity.Bookings;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class RefundCalculate {
    public BigDecimal calculateRefundAmount(Bookings booking, int refundPercent) {
        BigDecimal percent = BigDecimal.valueOf(refundPercent);
        BigDecimal divisor = new BigDecimal("100");

        // Công thức đúng: tiền hoàn = tiền cọc * % hoàn
        return booking.getTotalDepositAmount()
                .multiply(percent)
                .divide(divisor, 2, RoundingMode.HALF_UP);
    }

    public long calculateHoursBefore(LocalDateTime startTime) {
        return Math.max(
                Duration.between(LocalDateTime.now(), startTime).toHours(),
                0
        );
    }

    public int calculateRefundPercent(long hoursBefore) {
        if (hoursBefore >= 24) return 100;
        if (hoursBefore >= 12) return 60;
        return 0;
    }
}
