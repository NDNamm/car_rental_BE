package com.example.car_rental.service.impl;

import com.example.car_rental.dto.requestDTO.CarRequest.CarDetailRequest;
import com.example.car_rental.dto.requestDTO.CarRequest.CarRequest;
import com.example.car_rental.dto.requestDTO.CarRequest.UpdateCarStatusRequest;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingTimeResponse;
import com.example.car_rental.dto.responseDTO.CarResponse.CarDetailResponse;
import com.example.car_rental.dto.responseDTO.CarResponse.CarImageResponse;
import com.example.car_rental.dto.responseDTO.CarResponse.CarResponse;
import com.example.car_rental.entity.*;
import com.example.car_rental.entity.enums.CarStatusEnum;
import com.example.car_rental.exception.AppException;
import com.example.car_rental.exception.ErrorCode;
import com.example.car_rental.repository.*;
import com.example.car_rental.service.CarService;
import com.example.car_rental.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final FeatureRepository featureRepository;
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;
    private final BookingDetailRepository bookingDetailRepository;

    @Override
    @Transactional
    public void addCar(CarRequest request, MultipartFile[] images) {

        if (carRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.CAR_EXISTED);
        }

        Categories category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        CarDetailRequest carDetailRequest = request.getCarDetailRequest();

        Cars car = Cars.builder()
                .category(category)
                .name(request.getName())
                .licensePlate(request.getLicensePlate())
                .pricePerDay(request.getPricePerDay())
                .depositAmount(request.getDepositAmount())
                .status(request.getStatus() != null ? request.getStatus() : CarStatusEnum.AVAILABLE)
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .build();

        Set<Features> features = new HashSet<>();
        for (Long featureId : carDetailRequest.getFeatures()) {
            Features feature = featureRepository.findById(featureId)
                    .orElseThrow(() -> new AppException(ErrorCode.FEATURE_NOT_FOUND));
            features.add(feature);
        }

        CarDetail carDetail = CarDetail.builder()
                .seatCount(carDetailRequest.getSeatCount())
                .fuelType(carDetailRequest.getFuelType())
                .transmission(carDetailRequest.getTransmission())
                .manufactureYear(carDetailRequest.getManufactureYear())
                .address(carDetailRequest.getAddress())
                .city(carDetailRequest.getCity())
                .deliveryAvailable((carDetailRequest.getDeliveryAvailable() != null && carDetailRequest.getDeliveryAvailable()))
                .deliveryFee(carDetailRequest.getDeliveryFee())
                .features(features)
                .car(car)
                .build();

        car.setCarDetail(carDetail);

        List<CarImages> carImagesList =
                imageService.uploadCarImages(images, "cars", request.getName(), car);

        car.setCarImages(carImagesList);

        carRepository.save(car);
    }

    @Override
    @Transactional
    public void updateCar(Long carId, CarRequest request, MultipartFile[] images) {

        Cars car = carRepository.findById(carId)
                .orElseThrow(() -> new AppException(ErrorCode.CAR_NOT_FOUND));

        if (request.getName() != null) {
            if (carRepository.existsByNameAndIdNot(request.getName(), car.getId())) {
                throw new AppException(ErrorCode.CAR_EXISTED);
            }
            car.setName(request.getName());
        }

        if (request.getLicensePlate() != null) {
            car.setLicensePlate(request.getLicensePlate());
        }

        if (request.getPricePerDay() != null) {
            car.setPricePerDay(request.getPricePerDay());
        }

        if (request.getDepositAmount() != null) {
            car.setDepositAmount(request.getDepositAmount());
        }

        if (request.getDescription() != null) {
            car.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            car.setStatus(request.getStatus());
        }

        if (request.getCategoryId() != null) {
            Categories category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
            car.setCategory(category);
        }


        if (request.getImageIdsToDelete() != null && !request.getImageIdsToDelete().isEmpty()) {

            // 1. Tìm các đối tượng cần xóa
            List<CarImages> imagesToDelete = car.getCarImages().stream()
                    .filter(img -> request.getImageIdsToDelete().contains(img.getId()))
                    .collect(Collectors.toList());

            // 2. Xóa khỏi danh sách của car (Xóa trong DB)
            car.getCarImages().removeAll(imagesToDelete);

            // Ép Hibernate thực hiện lệnh DELETE xuống Database ngay lập tức
            carRepository.saveAndFlush(car);

            // 3. Sau khi DB đã xóa thành công, mới tiến hành xóa trên Cloud
            for (CarImages img : imagesToDelete) {
                try {
                    imageService.deleteImages(img.getPublicId());
                } catch (IOException e) {
                    // Log lỗi nhưng không nhất thiết phải throw exception làm rollback
                    // vì DB đã xóa rồi, ta chỉ đang dọn dẹp file rác trên cloud thôi.
                    log.error("Failed to delete image on Cloud: " + img.getPublicId());
                }
            }
        }

// 4. Thêm ảnh mới như bình thường
        if (images != null && images.length > 0) {
            List<CarImages> newCarImages = imageService.uploadCarImages(images, "cars", car.getName(), car);
            car.getCarImages().addAll(newCarImages);
        }

        updateCarDetail(car, request.getCarDetailRequest());

        carRepository.save(car);
    }

    private void updateCarDetail(Cars car, CarDetailRequest request) {

        if (request == null) return;

        CarDetail carDetail = car.getCarDetail();

        if (request.getSeatCount() != null)
            carDetail.setSeatCount(request.getSeatCount());

        if (request.getFuelType() != null)
            carDetail.setFuelType(request.getFuelType());

        if (request.getTransmission() != null)
            carDetail.setTransmission(request.getTransmission());

        if (request.getManufactureYear() != null)
            carDetail.setManufactureYear(request.getManufactureYear());

        if (request.getAddress() != null)
            carDetail.setAddress(request.getAddress());

        if (request.getCity() != null)
            carDetail.setCity(request.getCity());

        if (request.getDeliveryAvailable() != null)
            carDetail.setDeliveryAvailable(request.getDeliveryAvailable());

        if (request.getDeliveryFee() != null)
            carDetail.setDeliveryFee(request.getDeliveryFee());

        if (request.getFeatures() != null && !request.getFeatures().isEmpty()) {

            Set<Features> features = request.getFeatures().stream()
                    .map(id -> featureRepository.findById(id)
                            .orElseThrow(() -> new AppException(ErrorCode.FEATURE_NOT_FOUND)))
                    .collect(Collectors.toSet());

            carDetail.setFeatures(features);
        }
    }

    @Override
    @Transactional
    public void deleteCar(Long carId) {
        Cars car = carRepository.findById(carId)
                .orElseThrow(() -> new AppException(ErrorCode.CAR_NOT_FOUND));

        for (CarImages carImage : car.getCarImages()) {
            try {
                imageService.deleteImages(carImage.getPublicId());
            } catch (IOException e) {
                throw new AppException(ErrorCode.IMAGE_DELETE_FAILED);
            }
        }

        carRepository.delete(car);
    }

    @Override
    @Transactional
    public void updateStatusCar(Long carId, UpdateCarStatusRequest request) {
        Cars car = carRepository.findById(carId)
                .orElseThrow(() -> new AppException(ErrorCode.CAR_NOT_FOUND));

        if (request.getStatus() == null) {
            throw new AppException(ErrorCode.INVALID_CAR_STATUS);
        }

        car.setStatus(request.getStatus());
        carRepository.save(car);
    }

    @Override
    public Page<CarResponse> getAllCar(String name, String city, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
//        Page<Cars> carsPage;
//
//        if (name != null && !name.isEmpty()) {
//            carsPage = carRepository.findByCars(name, status, pageable);
//        } else {
//            carsPage = carRepository.findAll(pageable);
//        }

        Page<Cars> carsPage = carRepository.findCars(name, city, status, pageable);

        return carsPage.map(this::mapToCarResponse);
    }

    @Override
    public CarResponse getCarById(Long carId) {
        Cars car = carRepository.findById(carId)
                .orElseThrow(() -> new AppException(ErrorCode.CAR_NOT_FOUND));

        return mapToCarResponse(car);
    }

    @Override
    public Page<CarResponse> getAllCarsByCategoryId(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Cars> carsPage = carRepository.findByCategoryId(categoryId, pageable);

        return carsPage.map(this::mapToCarResponse);
    }

    @Override
    public List<BookingTimeResponse> getBookedRanges(Long carId) {
        return bookingDetailRepository.findBookedRanges(carId);
    }

    @Override
    public Page<String> getAllCities(String city, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<String> citiesPage = carRepository.findAllCities(city, pageable);
        return citiesPage;

    }

    private CarResponse mapToCarResponse(Cars car) {

        CarDetailResponse carDetailResponse = CarDetailResponse.builder()
                .id(car.getCarDetail().getId())
                .seatCount(car.getCarDetail().getSeatCount())
                .fuelType(car.getCarDetail().getFuelType())
                .transmission(car.getCarDetail().getTransmission())
                .manufactureYear(car.getCarDetail().getManufactureYear())
                .address(car.getCarDetail().getAddress())
                .city(car.getCarDetail().getCity())
                .deliveryAvailable(car.getCarDetail().isDeliveryAvailable())
                .deliveryFee(car.getCarDetail().getDeliveryFee())
                .features(car.getCarDetail().getFeatures().stream()
                        .map(Features::getName)
                        .collect(Collectors.toSet()))
                .build();

        List<CarImageResponse> carImageResponse = car.getCarImages().stream()
                .map(image -> CarImageResponse.builder()
                        .id(image.getId())
                        .imageUrl(image.getImageUrl())
                        .build())
                .collect(Collectors.toList());

        return CarResponse.builder()
                .id(car.getId())
                .name(car.getName())
                .licensePlate(car.getLicensePlate())
                .pricePerDay(car.getPricePerDay())
                .depositAmount(car.getDepositAmount())
                .status(car.getStatus())
                .tripCount(car.getTripCount())
                .totalReviews(car.getTotalReviews())
                .averageRating(car.getAverageRating())
                .description(car.getDescription())
                .createdAt(car.getCreatedAt())
                .categoryId(car.getCategory().getId())
                .categoryName(car.getCategory().getName())
                .carImageResponses(carImageResponse)
                .carDetailResponse(carDetailResponse)
                .build();
    }

}
