package com.example.car_rental.scheduler;

import com.example.car_rental.entity.BookingStatusHistory;
import com.example.car_rental.entity.Bookings;
import com.example.car_rental.entity.Users;
import com.example.car_rental.entity.enums.BookingStatusEnum;
import com.example.car_rental.entity.enums.CarStatusEnum;
import com.example.car_rental.entity.enums.NotificationTypeEnum;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.BookingStatusHistoryRepository;
import com.example.car_rental.repository.BookingsRepository;
import com.example.car_rental.repository.UserRepository;
import com.example.car_rental.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingScheduler {

    private final BookingsRepository bookingsRepository;
    private final BookingStatusHistoryRepository bookingStatusHistoryRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void autoCancelExpiredBookings() {

        List<Bookings> expiredBookings = bookingsRepository.findByStatusAndPaymentDeadlineBefore(
                BookingStatusEnum.PENDING,
                LocalDateTime.now());

        if (expiredBookings.isEmpty())
            return;

        for (Bookings booking : expiredBookings) {

            booking.setStatus(BookingStatusEnum.CANCELLED);

            booking.getBookingDetailsList().forEach(detail -> {
                detail.getCar().setStatus(CarStatusEnum.AVAILABLE);
            });

            Users systemUser = userRepository.findByEmail("system@gmail.com")
                    .orElseThrow(() -> new AppException(ErrorCode.SYSTEM_USER_NOT_FOUND));

            BookingStatusHistory his = BookingStatusHistory.builder()
                    .booking(booking)
                    .newStatus(BookingStatusEnum.CANCELLED)
                    .reason("Hết hạn thanh toán - Auto cancel")
                    .changedAt(LocalDateTime.now())
                    .changedBy(systemUser)
                    .build();

            bookingStatusHistoryRepository.save(his);
            notificationService.createNotification(booking.getUser().getId(), NotificationTypeEnum.BOOKING,
                    "Đơn đặt xe " + booking.getBookingCode() + " đã bị hủy do quá hạn thanh toán.", booking.getId());
            log.info("Auto cancel booking: {}", booking.getBookingCode());
        }

        bookingsRepository.saveAll(expiredBookings);
    }
}