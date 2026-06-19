package com.example.car_rental.scheduler;

import com.example.car_rental.entity.Bookings;
import com.example.car_rental.entity.enums.NotificationTypeEnum;
import com.example.car_rental.repository.BookingsRepository;
import com.example.car_rental.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CheckOverdueBookings {

    private final BookingsRepository bookingRepository;
    private final NotificationService notificationService;

    @Scheduled(fixedRate = 300000)
    @Transactional
    public void CheckOverdueBookings() {

        List<Bookings> overdueBookings = bookingRepository.findOverdueBookings(
                LocalDateTime.now());

        for (Bookings booking : overdueBookings) {

            notificationService.createNotification(
                    booking.getUser().getId(),
                    NotificationTypeEnum.LATE_RETURN,
                    "Xe của đơn " + booking.getBookingCode()
                            + " đã quá thời gian trả. Vui lòng hoàn trả xe hoặc liên hệ hỗ trợ.",
                    booking.getId());

            booking.setLateReminderSent(true);
        }

        bookingRepository.saveAll(overdueBookings);
    }
}