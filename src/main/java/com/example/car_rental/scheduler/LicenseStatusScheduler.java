package com.example.car_rental.scheduler;

import com.example.car_rental.entity.Users;
import com.example.car_rental.entity.Verifications;
import com.example.car_rental.entity.enums.LicenseStatusEnum;
import com.example.car_rental.entity.enums.NotificationTypeEnum;
import com.example.car_rental.entity.enums.VerStatusEnum;
import com.example.car_rental.repository.UserRepository;
import com.example.car_rental.repository.VerificationRepository;
import com.example.car_rental.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LicenseStatusScheduler {

    private final VerificationRepository verificationRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 5 22 * * ?")
    @Transactional
    public void updateLicenseStatus() {

        LocalDate today = LocalDate.now();
        List<Verifications> verifications = verificationRepository.findAll();
        for (Verifications verification : verifications) {
            log.info("Verification ID: {}", verification.getId());
            log.info("Expired Date: {}", verification.getLicenseExpiredDate());
            log.info("Current Status: {}", verification.getLicenseStatus());
            if (verification.getLicenseExpiredDate() == null) {
                continue;
            }
            long daysRemaining = ChronoUnit.DAYS.between(today, verification.getLicenseExpiredDate());
            log.info("Days Remaining: {}", daysRemaining);
            LicenseStatusEnum newStatus;

            if (daysRemaining < 0) {
                VerStatusEnum newVerStatus = VerStatusEnum.REJECTED;
                newStatus = LicenseStatusEnum.EXPIRED;
                verification.setStatus(newVerStatus);
                verification.setNote("Giấy phép lái xe đã hết hạn");
            } else if (daysRemaining <= 10) {
                newStatus = LicenseStatusEnum.EXPIRING_SOON;
            } else {
                newStatus = LicenseStatusEnum.VALID;
            }
            log.info("New Status: {}", newStatus);
            if (verification.getLicenseStatus() != newStatus) {
                log.info("Updating status for verification ID: {}", verification.getId());
                verification.setLicenseStatus(newStatus);
                verificationRepository.save(verification);
                sendNotification(verification.getUser().getId(), newStatus, verification.getId());
            }

            if (newStatus == LicenseStatusEnum.EXPIRED) {

                List<Users> admins = userRepository.findByRole_RoleName("ADMIN");

                admins.forEach(admin -> sendNotificationAdmin(admin.getId(), newStatus,
                        verification.getUser().getFullName(), verification.getId()));

                List<Users> staffs = userRepository.findByRole_RoleName("STAFF");

                staffs.forEach(staff -> sendNotificationAdmin(staff.getId(), newStatus,
                        verification.getUser().getFullName(), verification.getId()));
            }
        }
    }

    private void sendNotification(Long userId, LicenseStatusEnum newStatus, Long verId) {
        String message;
        switch (newStatus) {
            case EXPIRED:
                message = "Giấy phép lái xe của bạn đã hết hạn.";
                break;
            case EXPIRING_SOON:
                message = "Giấy phép lái xe của bạn sắp hết hạn.";
                break;
            default:
                return;
        }
        notificationService.createNotification(userId, NotificationTypeEnum.VERIFICATION, message, verId);
    }

    private void sendNotificationAdmin(Long adminId, LicenseStatusEnum newStatus, String fullName, Long verId) {
        notificationService.createNotification(adminId, NotificationTypeEnum.VERIFICATION,
                "GPLX của " + fullName + " đã hết hạn", verId);
    }
}