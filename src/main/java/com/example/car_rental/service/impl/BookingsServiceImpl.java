package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.BookingsRequest.BookingStatusRequest;
import com.example.car_rental.dto.requestDTO.BookingsRequest.BookingsRequest;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingDetailResponse;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingsResponse;
import com.example.car_rental.dto.responseDTO.CarResponse.CarImageResponse;
import com.example.car_rental.dto.responseDTO.CarResponse.CarResponse;
import com.example.car_rental.entity.BookingDetails;
import com.example.car_rental.entity.Bookings;
import com.example.car_rental.entity.Cars;
import com.example.car_rental.entity.enums.BookingStatusEnum;
import com.example.car_rental.entity.enums.CarStatusEnum;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.BookingsRepository;
import com.example.car_rental.repository.CarRepository;
import com.example.car_rental.service.BookingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingsServiceImpl implements BookingsService {

    final BookingsRepository bookingsRepository;
    final CarRepository carRepository;

    @Override
    public Page<BookingsResponse> getAllBookings(String bookingCode, String name, String phoneNumber, String status, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Bookings> bookingsPage;

        if (name == null && phoneNumber == null && status == null) {
            bookingsPage = bookingsRepository.findAll(pageable);
        } else {
            bookingsPage = bookingsRepository.searchBookings(bookingCode, name, phoneNumber, status, pageable);
        }

        return getBooking(bookingsPage);
    }

    private Page<BookingsResponse> getBooking(Page<Bookings> bookingsPage) {

        return bookingsPage.map(booking -> BookingsResponse.builder()
                .id(booking.getId())
                .bookingCode(booking.getBookingCode())
                .customerEmail(booking.getCustomerEmail())
                .customerName(booking.getCustomerName())
                .customerPhone(booking.getCustomerPhone())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .totalPrice(booking.getTotalPrice())
                .depositAmount(booking.getDepositAmount())
                .status(booking.getStatus())
                .bookingDetailsResponse(getBookingDetails(booking.getBookingDetailsList()))
                .build());
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
                    .pricePerDay(detail.getPricePerDay())
                    .subTotal(detail.getSubTotal())
                    // Lưu ý: nên là carResponse (số ít)
                    .carResponse(CarResponse.builder()
                            .id(currentCar.getId())
                            .name(currentCar.getName())
                            .carImageResponses(carImages)
                            .build())
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateBookingsStatus(BookingStatusRequest request, Long bookingId) {
        Bookings booking = bookingsRepository.findById(bookingId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOKING_NOT_FOUND));

        if (booking.getStatus() == BookingStatusEnum.CANCELLED || booking.getStatus() == BookingStatusEnum.COMPLETED) {
            throw new AppException(ErrorCode.BOOKING_ALREADY_CLOSED);
        }

        BookingStatusEnum current = booking.getStatus();
        BookingStatusEnum next = request.getStatus();
        if (current == next) return;

        boolean isValid = switch (current) {
            case PENDING -> next == BookingStatusEnum.CONFIRMED || next == BookingStatusEnum.CANCELLED;

            case CONFIRMED -> next == BookingStatusEnum.RENTED || next == BookingStatusEnum.CANCELLED;

            case RENTED -> next == BookingStatusEnum.COMPLETED; // Đang thuê thì bắt buộc phải trả xe mới xong

            case COMPLETED, CANCELLED -> false; // Đã xong hoặc đã hủy thì cấm sửa đổi

            default -> false;
        };

        if (!isValid) {
            throw new AppException(ErrorCode.INVALID_STATUS_TRANSITION);
        }

        booking.setStatus(next);
        bookingsRepository.save(booking);


//        // Nếu đơn chuyển sang "Đang thuê" -> Khóa xe lại (BUSY)
//        if (next == BookingStatusEnum.RENTED) {
//            booking.getBookingDetailsList().forEach(detail -> {
//                Cars car = detail.getCar();
//                car.setStatus(CarStatusEnum.RENTED);
//                carsRepository.save(car);
//            });
//        }
//
//        // Nếu đơn chuyển sang "Hoàn thành" hoặc "Hủy" -> Giải phóng xe (AVAILABLE)
//        if (next == BookingStatusEnum.COMPLETED || next == BookingStatusEnum.CANCELLED) {
//            booking.getBookingDetailsList().forEach(detail -> {
//                Cars car = detail.getCar();
//                car.setStatus(CarStatusEnum.AVAILABLE);
//                carsRepository.save(car);
//            });
//        }
    }


    @Override
    public void deleteBooking(Long bookingId) {

    }

    @Override
    public Long addBooking(BookingsRequest bookingsRequest) {
        return 0L;
    }

    @Override
    public void updateBooking(BookingsRequest bookingsRequest, Long bookingId) {

    }

    @Override
    public Page<BookingsResponse> getAllBookingUser(String bookingCode, String status, int page, int size) {
        return null;
    }

    @Override
    public void updateBookingUser(BookingsRequest bookingsRequest, Long bookingId) {

    }

    @Override
    public void cancelBooking(Long bookingId) {

    }
}
