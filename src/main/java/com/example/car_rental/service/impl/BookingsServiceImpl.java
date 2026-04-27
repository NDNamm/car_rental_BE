package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.BookingsRequest.BookingDetailRequest;
import com.example.car_rental.dto.requestDTO.BookingsRequest.BookingStatusRequest;
import com.example.car_rental.dto.requestDTO.BookingsRequest.BookingRequest;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingDetailResponse;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingStatusResponse;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingsResponse;
import com.example.car_rental.dto.responseDTO.CarResponse.CarImageResponse;
import com.example.car_rental.dto.responseDTO.CarResponse.CarResponse;
import com.example.car_rental.entity.*;
import com.example.car_rental.entity.enums.*;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.*;
import com.example.car_rental.service.BookingsService;
import com.example.car_rental.service.PaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    @Override
    public Page<BookingsResponse> getAllBookings(String bookingCode, String name, String phoneNumber, String status, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Bookings> bookingsPage;

        if (name == null && phoneNumber == null && status == null) {
            bookingsPage = bookingsRepository.findAll(pageable);
        } else {
            bookingsPage = bookingsRepository.searchBookings(bookingCode, name, phoneNumber, status, pageable);
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
    public String addBooking(String email, BookingRequest bookingRequest) {
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
                .customerEmail(bookingRequest.getCustomerEmail() == null
                        ? email
                        : bookingRequest.getCustomerEmail())

                .customerName(bookingRequest.getCustomerName() == null
                        ? user.getFullName()
                        : bookingRequest.getCustomerName())

                .customerPhone(bookingRequest.getCustomerPhone() == null
                        ? user.getPhoneNumber()
                        : bookingRequest.getCustomerPhone())
                .createdAt(LocalDateTime.now())
                .paymentDeadline(LocalDateTime.now().plusHours(1))
                .build();

        List<BookingDetails> detailsList = new ArrayList<>();
        BigDecimal totalOrderPrice = BigDecimal.ZERO;
        BigDecimal totalDeposit = BigDecimal.ZERO;

        for (BookingDetailRequest detailReq : bookingRequest.getBookingDetailRequest()) {

            if (detailReq.getCarRequest() == null || detailReq.getCarRequest().getId() == null) {
                throw new AppException(ErrorCode.INVALID_REQUEST);
            }
            calculateRentalDays(detailReq.getStartDate(), detailReq.getEndDate());

            Cars car = carRepository.findById(detailReq.getCarRequest().getId())
                    .orElseThrow(() -> new AppException(ErrorCode.CAR_NOT_FOUND));

            if (car.getStatus() != CarStatusEnum.AVAILABLE) {
                throw new AppException(ErrorCode.CAR_NOT_AVAILABLE);
            }

            car.setStatus(CarStatusEnum.RENTED);
            carRepository.save(car);

            // Tính số ngày thuê cho xe này
            long days = calculateRentalDays(detailReq.getStartDate(), detailReq.getEndDate());
            // Tính thành tiền (Giá x Ngày)
            BigDecimal subTotal = car.getPricePerDay().multiply(BigDecimal.valueOf(days));

            BookingDetails bookingDetail = BookingDetails.builder()
                    .car(car)
                    .booking(booking)
                    .startDate(detailReq.getStartDate())
                    .endDate(detailReq.getEndDate())
                    .rentalDays(days)
                    .pricePerDay(car.getPricePerDay())
                    .depositAmount(car.getDepositAmount())
                    .subTotal(subTotal)
                    .build();

            detailsList.add(bookingDetail);

            totalOrderPrice = totalOrderPrice.add(subTotal);
            totalDeposit = totalDeposit.add(detailReq.getDepositAmount());

        }

        booking.setBookingDetailsList(detailsList);
        booking.setTotalPrice(totalOrderPrice);
        booking.setTotalDepositAmount(totalDeposit);

        String qrBooking = paymentsService.generateQrUrl(booking);
        booking.setQrCodeUrl(qrBooking);
        Bookings savedBooking = bookingsRepository.save(booking);

        paymentsRepository.save(Payments.builder()
                .booking(savedBooking)
                .amount(totalDeposit)
                .status(PaymentStatusEnum.UNPAID)
                .paymentMethod(PaymentMethodEnum.BANK_TRANSFER)
                .paymentType(PaymentTypeEnum.DEPOSIT)
                .createdAt(LocalDateTime.now())
                .paymentContent(bookingCode)
                .build());

        return qrBooking;
    }

    @Override
    @Transactional
    public void updateBooking(String email, BookingRequest bookingRequest, Long bookingId) {
        Bookings booking = bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        if (!booking.getUser().getEmail().equals(email)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (booking.getStatus() != BookingStatusEnum.PENDING) {
            throw new AppException(ErrorCode.CAR_NOT_AVAILABLE);
        }

        BigDecimal newTotalPrice = BigDecimal.ZERO;
        BigDecimal newTotalDeposit = BigDecimal.ZERO;

        for (BookingDetailRequest detailReq : bookingRequest.getBookingDetailRequest()) {
            BookingDetails detail = bookingDetailRepository.findById(detailReq.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.DETAIL_NOT_FOUND));

            // Kiểm tra trùng lịch (loại trừ chính đơn hàng hiện tại)
            validateCarAvailability(detailReq.getCarRequest().getId(), bookingId,
                    detailReq.getStartDate(), detailReq.getEndDate());

            long days = calculateRentalDays(detailReq.getStartDate(), detailReq.getEndDate());

            // Cập nhật thông tin mới
            detail.setStartDate(detailReq.getStartDate());
            detail.setEndDate(detailReq.getEndDate());
            detail.setRentalDays(days);

            // Nếu đổi xe, cập nhật lại giá và tiền cọc
            Cars car = carRepository.findById(detailReq.getCarRequest().getId()).get();
            detail.setCar(car);
            detail.setPricePerDay(car.getPricePerDay());
            detail.setDepositAmount(car.getDepositAmount());

            BigDecimal subTotal = car.getPricePerDay().multiply(BigDecimal.valueOf(days));
            detail.setSubTotal(subTotal);

            newTotalPrice = newTotalPrice.add(subTotal);
            newTotalDeposit = newTotalDeposit.add(car.getDepositAmount());
        }

        if (bookingRequest.getCustomerName() != null && !bookingRequest.getCustomerName().isBlank()) {
            booking.setCustomerName(bookingRequest.getCustomerName());
        }

        if (bookingRequest.getCustomerPhone() != null && !bookingRequest.getCustomerPhone().isBlank()) {
            booking.setCustomerPhone(bookingRequest.getCustomerPhone());
        }
        booking.setTotalPrice(newTotalPrice);
        booking.setTotalDepositAmount(newTotalDeposit);

        bookingsRepository.save(booking);
    }

    // Hàm tính số ngày thuê // check tg
    private long calculateRentalDays(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }

        LocalDateTime now = LocalDateTime.now();

        if (start.isBefore(now)) {
            throw new AppException(ErrorCode.START_DATE_IN_PAST);
        }

        if (!end.isAfter(start)) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }

        long days = ChronoUnit.DAYS.between(start, end);

        return (days == 0) ? 1 : days;
    }

    private void validateCarAvailability(Long carId, Long currentBookingId, LocalDateTime start, LocalDateTime end) {
        long overlapCount = bookingDetailRepository.countOverlappingBookings(
                carId, currentBookingId, start, end);

        if (overlapCount > 0) {
            throw new AppException(ErrorCode.CAR_ALREADY_BOOKED);
        }
    }

    @Override
    public Page<BookingsResponse> getAllBookingUser(String email, String bookingCode, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Bookings> bookingsPage = bookingsRepository.findBookingsByUser(email, bookingCode, status, pageable);

        return bookingsPage.map(this::getBooking);
    }

    @Override
    @Transactional
    public void cancelBooking(String email, Long bookingId) {
        Bookings booking = bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        if (!booking.getUser().getEmail().equals(email)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (booking.getStatus() != BookingStatusEnum.PENDING) {
            throw new AppException(ErrorCode.CAR_NOT_AVAILABLE);
        }

        booking.getBookingDetailsList().forEach(detail -> {
            detail.getCar().setStatus(CarStatusEnum.AVAILABLE);
            carRepository.save(detail.getCar());
        });

        booking.setStatus(BookingStatusEnum.CANCELLED);
        bookingsRepository.save(booking);
    }

    @Override
    public BookingsResponse getBooking(Long bookingId) {
        return null;
    }
}
