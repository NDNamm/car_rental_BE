package com.example.car_rental.service.impl;

import com.example.car_rental.domain.RefundCalculate;
import com.example.car_rental.dto.requestDTO.BookingsRequest.BookingDetailRequest;
import com.example.car_rental.dto.requestDTO.BookingsRequest.BookingStatusRequest;
import com.example.car_rental.dto.requestDTO.BookingsRequest.BookingRequest;
import com.example.car_rental.dto.requestDTO.BookingsRequest.ReplaceCarRequest;
import com.example.car_rental.dto.requestDTO.RefundRequest;
import com.example.car_rental.dto.requestDTO.handoverRequest.HandoverRequest;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingCodeResponse;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingDetailResponse;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingStatusResponse;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingsResponse;
import com.example.car_rental.dto.responseDTO.CarResponse.CarImageResponse;
import com.example.car_rental.dto.responseDTO.CarResponse.CarResponse;
import com.example.car_rental.dto.responseDTO.RefundResponse;
import com.example.car_rental.entity.*;
import com.example.car_rental.entity.enums.*;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.*;
import com.example.car_rental.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingsServiceImpl implements BookingsService {

    final BookingsRepository bookingsRepository;
    final CarRepository carRepository;
    final UserRepository userRepository;
    final BookingDetailRepository bookingDetailRepository;
    final PaymentsRepository paymentsRepository;
    final PaymentsService paymentsService;
    final VerificationRepository verificationRepository;
    final BookingStatusHistoryRepository bookingStatusHistoryRepository;
    final RefundService refundService;
    final RefundCalculate refundCalculate;
    final ReviewRepository reviewRepository;
    final HandoverService handoverService;
    final InvoiceService invoiceService;
    final NotificationService notificationService;


    @Override
    public Page<BookingsResponse> getAllBookings(String search, String status, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Bookings> bookingsPage;

        boolean noFilter =
                (search == null || search.isBlank()) &&
//                        (email == null || email.isBlank()) &&
//                        (name == null || name.isBlank()) &&
//                        (phoneNumber == null || phoneNumber.isBlank()) &&
                        (status == null || status.isBlank());

        if (noFilter) {
            bookingsPage = bookingsRepository.searchBookings(null, null, pageable);
        } else {
            BookingStatusEnum statusEnum = null;

            if (status != null && !status.isBlank()) {
                statusEnum = BookingStatusEnum.valueOf(status.toUpperCase());
            }
            bookingsPage = bookingsRepository.searchBookings(search, statusEnum, pageable);
        }

        return bookingsPage.map(this::getBooking);
    }

    @Override
    public BookingsResponse getBookingById(Long bookingId) {
        Bookings bookings = bookingsRepository.findBookingById(bookingId);
        return getBooking(bookings);
    }

    private BookingsResponse getBooking(Bookings booking) {

        return BookingsResponse.builder()
                .id(booking.getId())
                .bookingCode(booking.getBookingCode())
                .customerEmail(booking.getCustomerEmail())
                .customerName(booking.getCustomerName())
                .customerPhone(booking.getCustomerPhone())
                .totalPrice(booking.getTotalPrice())
                .totalDepositAmount(booking.getTotalDepositAmount())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .nameStaff(booking.getUser().getFullName())
                .note(booking.getNote())
                .paymentDeadline(booking.getPaymentDeadline())
                .qrCodeUrl(booking.getQrCodeUrl())
                .bookingDetailResponse(getBookingDetails(booking.getBookingDetailsList()))
                .bookingStatusResponses(getBookingStatusHistory(booking.getBookingStatusHistory()))
                .invoicesResponse(invoiceService.getInvoiceByBookingId(booking.getId()))
                .build();
    }

    private List<BookingStatusResponse> getBookingStatusHistory(List<BookingStatusHistory> historyList) {
        if (historyList == null) return Collections.emptyList();

        return historyList.stream().map(history -> BookingStatusResponse.builder()
                .id(history.getId())
                .oldStatus(history.getOldStatus())
                .newStatus(history.getNewStatus())
                .reason(history.getReason())
                .note(history.getNote())
                .changedAt(history.getChangedAt())
                .changedBy(history.getChangedBy().getFullName())
                .build()).collect(Collectors.toList());
    }

    private List<BookingDetailResponse> getBookingDetails(List<BookingDetails> bookingDetailsList) {
        if (bookingDetailsList == null) return Collections.emptyList();

        return bookingDetailsList.stream().map(detail -> {
            Cars currentCar = detail.getCar();

            List<CarImageResponse> carImages = currentCar.getCarImages().stream()
                    .map(image -> CarImageResponse.builder()
                            .id(image.getId())
                            .imageUrl(image.getImageUrl())
                            .build())
                    .collect(Collectors.toList());

            // 3. Build BookingDetailResponse
            return BookingDetailResponse.builder()
                    .id(detail.getId())
                    .rentalDays(detail.getRentalDays())
                    .startDate(detail.getStartDate())
                    .endDate(detail.getEndDate())
                    .pricePerDay(detail.getPricePerDay())
                    .subTotal(detail.getSubTotal())
                    .depositAmount(detail.getDepositAmount())
                    .carResponse(CarResponse.builder()
                            .id(currentCar.getId())
                            .name(currentCar.getName())
                            .licensePlate(currentCar.getLicensePlate())
                            .pricePerDay(currentCar.getPricePerDay())
                            .depositAmount(currentCar.getDepositAmount())
                            .categoryName(currentCar.getCategory().getName())
                            .carImageResponses(carImages)
                            .build())
                    .handoverResponses(handoverService.getHandoversByBookingDetailId(detail.getId()))
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateBookingsStatus(BookingStatusRequest request, Long bookingId, HandoverRequest handoverRequest, MultipartFile[] images) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users staff = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Bookings booking = bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        if (booking.getStatus() == BookingStatusEnum.CANCELLED || booking.getStatus() == BookingStatusEnum.COMPLETED) {
            throw new AppException(ErrorCode.BOOKING_ALREADY_CLOSED);
        }

        BookingStatusEnum current = booking.getStatus();
        BookingStatusEnum next = request.getStatus();
        if (current == next) return;

        // Kiểm tra tính hợp lệ của việc chuyển trạng thái
        boolean isValid = switch (current) {
            case PENDING -> next == BookingStatusEnum.CONFIRMED || next == BookingStatusEnum.CANCELLED;
            case CONFIRMED -> next == BookingStatusEnum.RENTED || next == BookingStatusEnum.CANCELLED;
            case RENTED -> next == BookingStatusEnum.RETURNING || next == BookingStatusEnum.CANCELLED;
            case RETURNING -> next == BookingStatusEnum.COMPLETED;
            default -> false;
        };

        if (!isValid) throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);

        booking.setStatus(next);
        bookingsRepository.save(booking);

        // XỬ LÝ LOGIC CHI TIẾT THEO TRẠNG THÁI MỚI
        booking.getBookingDetailsList().forEach(detail -> {
            Cars car = detail.getCar();
            log.info("Car {} trip count increased to {}", car.getId(), car.getTripCount());
            // TRƯỜNG HỢP: GIAO XE CHO KHÁCH
            if (next == BookingStatusEnum.RENTED) {
                car.setStatus(CarStatusEnum.RENTED);

                handoverService.processHandover(detail.getId(), staff, handoverRequest, images);
            } else if (next == BookingStatusEnum.RETURNING) {
                handoverService.processHandover(detail.getId(), staff, handoverRequest, images);
            }

            // TRƯỜNG HỢP: KHÁCH TRẢ XE (HOÀN THÀNH)
            else if (next == BookingStatusEnum.COMPLETED) {
                car.setStatus(CarStatusEnum.AVAILABLE);
                car.setTripCount(car.getTripCount() + 1);
                invoiceService.recalculateInvoice(booking.getId());
            }

            // TRƯỜNG HỢP: HỦY ĐƠN
            else if (next == BookingStatusEnum.CANCELLED) {
                car.setStatus(CarStatusEnum.AVAILABLE);
            }
            carRepository.save(car);
            createHistoryStatusChange(booking, next, request.getReason(), request.getNote());
            createNotificationBooking(booking, next);
        });
    }

    @Override
    @Transactional
    public void replaceCar(Long bookingId, ReplaceCarRequest request) {
        Bookings booking = bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        if(!booking.getStatus().equals(BookingStatusEnum.CONFIRMED)){
            throw new AppException(ErrorCode.BOOKING_ALREADY_PROCESSED);
        }

        BookingDetails bookingDetail = bookingDetailRepository.findById(request.getBookingDetailId())
                .orElseThrow(() -> new AppException(ErrorCode.DETAIL_NOT_FOUND));
        Cars oldCar = bookingDetail.getCar();
        Cars newCar = carRepository.findById(request.getNewCarId())
                .orElseThrow(() -> new AppException(ErrorCode.CAR_NOT_FOUND));

        LocalDateTime startDate = bookingDetail.getStartDate();
        LocalDateTime endDate = bookingDetail.getEndDate();

        // 🔥 check xe mới có bị trùng lịch không
        validateCarAvailability(request.getNewCarId(), bookingId, startDate, endDate);

        // 🔥 chỉ đổi nếu hợp lệ
        bookingDetail.setCar(newCar);
        bookingDetailRepository.save(bookingDetail);

        // ⚠️ chỉ update status nếu thật sự cần (không ép logic cứng)
        newCar.setStatus(CarStatusEnum.RENTED);

        carRepository.save(newCar);
        carRepository.save(oldCar);
        recalculateBookingPrice(booking);

        invoiceService.recalculateInvoice(booking.getId());
        createHistoryStatusChange(
                booking,
                booking.getStatus(),
                "Đổi xe trong đơn đặt xe",
                "Xe cũ: " + oldCar.getName() + " → Xe mới: " + newCar.getName()
        );

        notificationService.createNotification(
                booking.getUser().getId(),
                NotificationTypeEnum.BOOKING,
                "Đơn " + booking.getBookingCode() + " đã được đổi sang xe mới: " + newCar.getName()
        );
    }


    @Override
    @Transactional
    public void deleteBooking(Long bookingId) {
        Bookings booking = bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        bookingsRepository.delete(booking);
    }

    //USER
    @Override
    @Transactional
    public BookingCodeResponse addBooking(String email, BookingRequest request) {
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Verifications verification = verificationRepository.findByUserId(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_VERIFIED));
        if (verification.getStatus() != VerStatusEnum.APPROVED) {
            throw new AppException(ErrorCode.VERIFICATION_PENDING_OR_REJECTED);
        }
        String bookingCode = "BK_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Bookings booking = Bookings.builder()
                .user(user)
                .bookingCode(bookingCode)
                .status(BookingStatusEnum.PENDING)
                .customerEmail(user.getEmail())
                .customerName(request.getCustomerName() != null ? request.getCustomerName() : user.getFullName())
                .customerPhone(request.getCustomerPhone() != null ? request.getCustomerPhone() : user.getPhoneNumber())
                .createdAt(LocalDateTime.now())
                .paymentDeadline(LocalDateTime.now().plusHours(1))
                .build();

        List<BookingDetails> detailsList = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal totalDeposit = BigDecimal.ZERO;

        for (BookingDetailRequest d : request.getBookingDetailRequest()) {
            if (d.getCarRequest() == null || d.getCarRequest().getId() == null) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }
            if (d.getStartDate() == null || d.getEndDate() == null) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }
            long days = calculateRentalDays(d.getStartDate(), d.getEndDate());

            Cars car = carRepository.findById(d.getCarRequest().getId())
                    .orElseThrow(() -> new AppException(ErrorCode.CAR_NOT_FOUND));
            if (car.getStatus() == CarStatusEnum.MAINTENANCE) {
                throw new AppException(ErrorCode.CAR_NOT_AVAILABLE);
            }
            // 🔥 check trùng lịch (thuê theo ngày)
            validateCarAvailability(car.getId(), null, d.getStartDate(), d.getEndDate());
            BigDecimal subTotal = car.getPricePerDay().multiply(BigDecimal.valueOf(days));

            BookingDetails detail = BookingDetails.builder()
                    .booking(booking)
                    .car(car)
                    .startDate(d.getStartDate())
                    .endDate(d.getEndDate())
                    .rentalDays(days)
                    .pricePerDay(car.getPricePerDay())
                    .depositAmount(car.getDepositAmount())
                    .subTotal(subTotal)
                    .build();

            detailsList.add(detail);

            totalPrice = totalPrice.add(subTotal);
            totalDeposit = totalDeposit.add(car.getDepositAmount());
        }

        booking.setBookingDetailsList(detailsList);
        booking.setTotalPrice(totalPrice);
        booking.setTotalDepositAmount(totalDeposit);
        String qr = paymentsService.generateQrUrl(booking);
        booking.setQrCodeUrl(qr);

        bookingsRepository.save(booking);

        paymentsService.createPayment(booking);
        invoiceService.generateInvoice(booking.getId());
        createNotificationBooking(booking, booking.getStatus());
        createHistoryStatusChange(booking, BookingStatusEnum.PENDING, "Đã tạo đơn đặt xe thành công", null);

        return BookingCodeResponse.builder()
                .bookingCode(bookingCode)
                .qrCodeUrl(qr)
                .build();
    }

    @Override
    @Transactional
    public BookingCodeResponse updateBooking(String email, BookingRequest request, Long bookingId) {

        Bookings booking = bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        if (!booking.getUser().getEmail().equals(email)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (booking.getStatus() != BookingStatusEnum.PENDING) {
            throw new AppException(ErrorCode.CANNOT_UPDATE_BOOKING);
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        BigDecimal totalDeposit = BigDecimal.ZERO;

        for (BookingDetailRequest d : request.getBookingDetailRequest()) {

            if (d.getId() == null) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }

            if (d.getCarRequest() == null || d.getCarRequest().getId() == null) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }

            BookingDetails detail = bookingDetailRepository.findById(d.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.DETAIL_NOT_FOUND));

            Long carId = d.getCarRequest().getId();

            // 🔥 check trùng lịch (loại trừ chính booking hiện tại)
            validateCarAvailability(carId, bookingId, d.getStartDate(), d.getEndDate());

            long days = calculateRentalDays(d.getStartDate(), d.getEndDate());

            Cars car = carRepository.findById(carId)
                    .orElseThrow(() -> new AppException(ErrorCode.CAR_NOT_FOUND));

            detail.setCar(car);
            detail.setStartDate(d.getStartDate());
            detail.setEndDate(d.getEndDate());
            detail.setRentalDays(days);
            detail.setPricePerDay(car.getPricePerDay());
            detail.setDepositAmount(car.getDepositAmount());

            BigDecimal subTotal = car.getPricePerDay().multiply(BigDecimal.valueOf(days));
            detail.setSubTotal(subTotal);

            totalPrice = totalPrice.add(subTotal);
            totalDeposit = totalDeposit.add(car.getDepositAmount());
        }

        if (request.getCustomerName() != null && !request.getCustomerName().isBlank()) {
            booking.setCustomerName(request.getCustomerName());
        }
        if (request.getCustomerPhone() != null && !request.getCustomerPhone().isBlank()) {
            booking.setCustomerPhone(request.getCustomerPhone());
        }
        booking.setTotalPrice(totalPrice);
        booking.setTotalDepositAmount(totalDeposit);
        bookingsRepository.save(booking);
        createHistoryStatusChange(booking, BookingStatusEnum.PENDING, "Bạn đã cập nhật thông tin đặt xe", null);
        return BookingCodeResponse.builder()
                .bookingCode(booking.getBookingCode())
                .qrCodeUrl(booking.getQrCodeUrl())
                .build();
    }

    // Hàm tính số ngày thuê // check tg
    private long calculateRentalDays(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }
        if (!end.isAfter(start)) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }
        LocalDateTime now = LocalDateTime.now();
        if (start.isBefore(now)) {
            throw new AppException(ErrorCode.START_DATE_IN_PAST);
        }
        long days = ChronoUnit.DAYS.between(
                start.toLocalDate(),
                end.toLocalDate()
        );
        return (days == 0) ? 1 : days;
    }

    private void validateCarAvailability(Long carId, Long bookingId, LocalDateTime start, LocalDateTime end) {
        long count = bookingDetailRepository.countOverlappingBookings(carId, bookingId, start, end);
        if (count > 0) {
            throw new AppException(ErrorCode.CAR_ALREADY_BOOKED_IN_THIS_TIME);
        }
    }

    @Override
    public Page<BookingsResponse> getAllBookingUser(String email, String bookingCode, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Bookings> bookingsPage;
        boolean noFilter =
                (bookingCode == null || bookingCode.isBlank()) &&
                        (status == null || status.isBlank());
        if (noFilter) {
            bookingsPage = bookingsRepository.findBookingsByUser(email, null, null, pageable);
        } else {
            BookingStatusEnum statusEnum = null;

            if (status != null && !status.isBlank()) {
                statusEnum = BookingStatusEnum.valueOf(status.toUpperCase());
            }
            bookingsPage = bookingsRepository.findBookingsByUser(email, bookingCode, statusEnum, pageable);
        }
        // ✅ LẤY LIST BOOKING IDS
        List<Long> bookingIds = bookingsPage.getContent()
                .stream()
                .map(Bookings::getId)
                .toList();
        // ✅ QUERY NHỮNG BOOKING ĐÃ REVIEW
        List<Long> reviewedIds = reviewRepository.findBookingIdsHaveReview(bookingIds);
        Set<Long> reviewedSet = new HashSet<>(reviewedIds);
        // ✅ MAP + SET hasReview
        return bookingsPage.map(booking -> {
            BookingsResponse res = getBooking(booking);
            res.setHasReview(reviewedSet.contains(booking.getId()));
            return res;
        });
    }

    @Override
    @Transactional
    public void cancelBooking(String email, Long bookingId, RefundRequest request, MultipartFile qrCode) {

        Bookings booking = bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        if (!booking.getUser().getEmail().equals(email)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (booking.getStatus() == BookingStatusEnum.RENTED
                || booking.getStatus() == BookingStatusEnum.COMPLETED
                || booking.getStatus() == BookingStatusEnum.CANCELLED) {
            throw new AppException(ErrorCode.CANNOT_CANCEL_BOOKING);
        }

        if (booking.getStatus() == BookingStatusEnum.PENDING) {
            booking.setStatus(BookingStatusEnum.CANCELLED);
            BookingStatusHistory history = BookingStatusHistory.builder()
                    .booking(booking)
                    .oldStatus(BookingStatusEnum.PENDING)
                    .newStatus(BookingStatusEnum.CANCELLED)
                    .reason("Khách hàng hủy đơn")
                    .changedAt(LocalDateTime.now())
                    .changedBy(booking.getUser())
                    .build();

            bookingStatusHistoryRepository.save(history);
            bookingsRepository.save(booking);

            invoiceService.recalculateInvoice(booking.getId());

            return;
        }

        if (booking.getStatus() == BookingStatusEnum.CONFIRMED) {
            LocalDateTime startDate = booking.getBookingDetailsList().get(0).getStartDate();
            long hoursBefore = refundCalculate.calculateHoursBefore(startDate);
            int refundPercent = refundCalculate.calculateRefundPercent(hoursBefore);

            if (refundPercent > 0) {
                refundService.createRefund(booking, request, qrCode);
            }
        }

        // update car
        booking.getBookingDetailsList().forEach(detail -> {
            detail.getCar().setStatus(CarStatusEnum.AVAILABLE);
            carRepository.save(detail.getCar());
        });

        createHistoryStatusChange(booking, BookingStatusEnum.CANCELLED, "Khách hàng hủy đơn", request.getNote());
        booking.setStatus(BookingStatusEnum.CANCELLED);
        bookingsRepository.save(booking);

    }

    @Override
    public BookingsResponse getBookingUserById(String email, Long bookingId) {

        Bookings bookings = bookingsRepository.findByIdAndUserEmail(bookingId, email);
        return getBooking(bookings);
    }

    private void createHistoryStatusChange(Bookings booking, BookingStatusEnum newStatus, String reason, String note) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users staff = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        BookingStatusHistory history = BookingStatusHistory.builder()
                .booking(booking)
                .oldStatus(booking.getStatus())
                .newStatus(newStatus)
                .reason(reason)
                .note(note)
                .changedAt(LocalDateTime.now())
                .changedBy(staff)
                .build();

        bookingStatusHistoryRepository.save(history);
    }

    private void createNotificationBooking(Bookings booking, BookingStatusEnum newStatus) {
        String message = switch (newStatus) {
            case CONFIRMED -> "Đơn đặt xe " + booking.getBookingCode() + " của bạn đã được xác nhận.";
            case RENTED ->
                    "Chúc mừng! Bạn đã nhận xe thành công cho đơn hàng " + booking.getBookingCode() + ". Chúc bạn có những trải nghiệm tuyệt vời với chiếc xe!";
            case RETURNING ->
                    "Bạn đang trong quá trình trả xe cho đơn hàng " + booking.getBookingCode();
            case COMPLETED ->
                    "Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi! Đơn hàng " + booking.getBookingCode() + " đã hoàn thành. Chúng tôi hy vọng bạn hài lòng với trải nghiệm thuê xe và mong được phục vụ bạn trong những lần tiếp theo.";
            case CANCELLED ->
                    "Đơn đặt xe " + booking.getBookingCode() + " của bạn đã bị hủy. Nếu bạn có bất kỳ thắc mắc nào, vui lòng liên hệ với chúng tôi để được hỗ trợ.";
            default -> "Bạn đã tạo đơn đặt xe thành công với mã " + booking.getBookingCode() + ". Vui lòng thanh toán trong vong 1 giờ để xác nhận đơn hàng.";
        };

        notificationService.createNotification(booking.getUser().getId(), NotificationTypeEnum.BOOKING, message);
    }

    private void recalculateBookingPrice(Bookings booking) {

        BigDecimal totalPrice = BigDecimal.ZERO;
        for (BookingDetails detail : booking.getBookingDetailsList()) {
            Cars car = detail.getCar();
            long days = ChronoUnit.DAYS.between(detail.getStartDate().toLocalDate(), detail.getEndDate().toLocalDate());
            days = (days <= 0) ? 1 : days;
            BigDecimal subTotal = car.getPricePerDay().multiply(BigDecimal.valueOf(days));

            detail.setPricePerDay(car.getPricePerDay());
            detail.setSubTotal(subTotal);
            totalPrice = totalPrice.add(subTotal);
        }
        booking.setTotalPrice(totalPrice);
    }

}
