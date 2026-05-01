package com.example.car_rental.service.impl;

import com.example.car_rental.domain.RefundCalculate;
import com.example.car_rental.dto.requestDTO.BookingsRequest.BookingDetailRequest;
import com.example.car_rental.dto.requestDTO.BookingsRequest.BookingStatusRequest;
import com.example.car_rental.dto.requestDTO.BookingsRequest.BookingRequest;
import com.example.car_rental.dto.requestDTO.RefundRequest;
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
import com.example.car_rental.service.BookingsService;
import com.example.car_rental.service.PaymentsService;
import com.example.car_rental.service.RefundService;
import lombok.RequiredArgsConstructor;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    final CarHandoverRepository carHandoverRepository;
    final BookingStatusHistoryRepository bookingStatusHistoryRepository;
    final RefundService refundService;
    final RefundCalculate refundCalculate;


    @Override
    public Page<BookingsResponse> getAllBookings(String bookingCode, String email, String name, String phoneNumber, String status, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Bookings> bookingsPage;

        boolean noFilter =
                (bookingCode == null || bookingCode.isBlank()) &&
                        (email == null || email.isBlank()) &&
                        (name == null || name.isBlank()) &&
                        (phoneNumber == null || phoneNumber.isBlank()) &&
                        (status == null || status.isBlank());

        if (noFilter) {
            bookingsPage = bookingsRepository.findAll(pageable);
        } else {
            BookingStatusEnum statusEnum = null;

            if (status != null && !status.isBlank()) {
                statusEnum = BookingStatusEnum.valueOf(status.toUpperCase());
            }
            bookingsPage = bookingsRepository.searchBookings(bookingCode, email, name, phoneNumber, statusEnum, pageable);
        }

        return bookingsPage.map(this::getBooking);
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
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateBookingsStatus(BookingStatusRequest request, Long bookingId) {
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
            case RENTED -> next == BookingStatusEnum.COMPLETED;
            default -> false;
        };

        if (!isValid) throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);

        booking.setStatus(next);
        bookingsRepository.save(booking);

        // XỬ LÝ LOGIC CHI TIẾT THEO TRẠNG THÁI MỚI
        booking.getBookingDetailsList().forEach(detail -> {
            Cars car = detail.getCar();

            // TRƯỜNG HỢP: GIAO XE CHO KHÁCH
            if (next == BookingStatusEnum.RENTED) {
                car.setStatus(CarStatusEnum.RENTED);

                // Tìm hoặc tạo mới biên bản (Lúc này chắc chắn là tạo mới vì mới bắt đầu đi)
                CarHandovers handover = carHandoverRepository.findByBookingDetails_Id(detail.getId())
                        .orElse(new CarHandovers());

                handover.setReason(request.getReason());
                handover.setHandoverTime(LocalDateTime.now()); // GIỜ GIAO XE
                handover.setCarConditionAfter(request.getNote()); // Tình trạng sau khi bàn giao
                handover.setStaff(staff);
                handover.setBookingDetails(detail);
                carHandoverRepository.save(handover);
            }

            // TRƯỜNG HỢP: KHÁCH TRẢ XE (HOÀN THÀNH)
            else if (next == BookingStatusEnum.COMPLETED) {
                car.setStatus(CarStatusEnum.AVAILABLE);
                car.setTripCount(car.getTripCount() + 1);

                // TÌM LẠI biên bản đã tạo lúc Giao xe để cập nhật
                CarHandovers handover = carHandoverRepository.findByBookingDetails_Id(detail.getId())
                        .orElseThrow(() -> new AppException(ErrorCode.HANDOVER_NOT_FOUND));

                handover.setReturnTime(LocalDateTime.now());
                handover.setCarConditionBefore(request.getNote());
                handover.setStaff(staff);
                carHandoverRepository.save(handover);
            }

            // TRƯỜNG HỢP: HỦY ĐƠN
            else if (next == BookingStatusEnum.CANCELLED) {
                car.setStatus(CarStatusEnum.AVAILABLE);
            }

            carRepository.save(car);

            BookingStatusHistory history = BookingStatusHistory.builder()
                    .booking(booking)
                    .oldStatus(current)
                    .newStatus(next)
                    .reason(request.getReason())
                    .note(request.getNote())
                    .changedAt(LocalDateTime.now())
                    .changedBy(staff)
                    .build();

            bookingStatusHistoryRepository.save(history);
        });
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
            validateCarAvailability(
                    car.getId(),
                    null,
                    d.getStartDate(),
                    d.getEndDate()
            );

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

        Bookings saved = bookingsRepository.save(booking);

        paymentsRepository.save(Payments.builder()
                .booking(saved)
                .amount(totalDeposit)
                .status(PaymentStatusEnum.UNPAID)
                .paymentMethod(PaymentMethodEnum.BANK_TRANSFER)
                .paymentType(PaymentTypeEnum.DEPOSIT)
                .createdAt(LocalDateTime.now())
                .paymentContent(bookingCode)
                .build());

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

    private void validateCarAvailability(Long carId,
                                         Long bookingId,
                                         LocalDateTime start,
                                         LocalDateTime end) {

        long count = bookingDetailRepository.countOverlappingBookings(carId, bookingId, start, end);
        if (count > 0) {
            throw new AppException(ErrorCode.CAR_ALREADY_BOOKED_IN_THIS_TIME);
        }
    }

    @Override
    public Page<BookingsResponse> getAllBookingUser(String email, String bookingCode, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Bookings> bookingsPage = null;
        boolean noFilter =
                (bookingCode == null || bookingCode.isBlank()) &&
                        (status == null || status.isBlank());

        if (noFilter) {
            bookingsPage = bookingsRepository.findAll(pageable);
        } else {
            BookingStatusEnum statusEnum = null;

            if (status != null && !status.isBlank()) {
                statusEnum = BookingStatusEnum.valueOf(status.toUpperCase());
            }
            bookingsPage = bookingsRepository.findBookingsByUser(email, bookingCode, statusEnum, pageable);
        }

        return bookingsPage.map(this::getBooking);
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

        RefundResponse refundResponse = null;

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

        // history
        BookingStatusHistory history = BookingStatusHistory.builder()
                .booking(booking)
                .oldStatus(booking.getStatus())
                .newStatus(BookingStatusEnum.CANCELLED)
                .reason("Khách hàng hủy đơn")
                .changedAt(LocalDateTime.now())
                .changedBy(booking.getUser())
                .build();

        bookingStatusHistoryRepository.save(history);

        booking.setStatus(BookingStatusEnum.CANCELLED);
        bookingsRepository.save(booking);

    }

}
