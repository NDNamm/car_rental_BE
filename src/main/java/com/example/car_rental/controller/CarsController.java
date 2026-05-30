package com.example.car_rental.controller;

import com.example.car_rental.dto.requestDTO.CarRequest.CarRequest;
import com.example.car_rental.dto.requestDTO.CarRequest.UpdateCarStatusRequest;
import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.BookingResponse.BookingTimeResponse;
import com.example.car_rental.dto.responseDTO.CarResponse.CarResponse;
import com.example.car_rental.service.CarService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cars")
public class CarsController {

    private final CarService carService;

    @GetMapping("")
    public ApiResponse<Page<CarResponse>> getAllCars(@RequestParam(defaultValue = "0", required = false) int page,
                                                     @RequestParam(defaultValue = "5") int size,
                                                     @RequestParam(required = false) String name,
                                                     @RequestParam(required = false) String city,
                                                     @RequestParam(required = false) String status) {
        return ApiResponse.<Page<CarResponse>>builder()
                .code(200)
                .data(carService.getAllCar(name, city, status, page, size))
                .build();
    }

    @GetMapping("/city")
    public ApiResponse<Page<String>> getAllCity(@RequestParam(required = false) String city,
                                                @RequestParam(defaultValue = "0", required = false) int page,
                                                @RequestParam(defaultValue = "5") int size) {
        return ApiResponse.<Page<String>>builder()
                .code(200)
                .data(carService.getAllCities(city, page, size))
                .build();
    }

    @GetMapping("/{carId}")
    public ApiResponse<CarResponse> getCar(@PathVariable(value = "carId") Long carId) {
        return ApiResponse.<CarResponse>builder()
                .code(200)
                .data(carService.getCarById(carId))
                .build();
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Void> addCar(@RequestPart("carRequest") String request,
                                    @RequestPart("images") MultipartFile[] images) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        CarRequest carRequest = objectMapper.readValue(request, CarRequest.class);
        carService.addCar(carRequest, images);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Create car successful")
                .build();
    }


    @PutMapping(value = "/{carId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Void> updateCar(@PathVariable(value = "carId") Long carId,
                                       @RequestPart(value = "carRequest", required = false) String request,
                                       @RequestPart(value = "images", required = false) MultipartFile[] images) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        CarRequest carRequest = objectMapper.readValue(request, CarRequest.class);
        carService.updateCar(carId, carRequest, images);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Update car successful")
                .build();
    }

    @PatchMapping(value = "/{carId}")
    public ApiResponse<Void> updateCar(@PathVariable(value = "carId") Long carId,
                                       @RequestBody UpdateCarStatusRequest request) {

        carService.updateStatusCar(carId, request);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Update car successful")
                .build();
    }

    @DeleteMapping("/{carId}")
    public ApiResponse<Void> deleteCar(@PathVariable("carId") Long carId) {

        carService.deleteCar(carId);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete car successful")
                .build();
    }

    @GetMapping("/{carId}/booked-times")
    public ApiResponse<List<BookingTimeResponse>> getBookedTimes(@PathVariable Long carId) {
        List<BookingTimeResponse> ranges = carService.getBookedRanges(carId);

        return ApiResponse.<List<BookingTimeResponse>>builder()
                .data(ranges)
                .message("Lấy danh sách thời gian đã đặt thành công")
                .build();
    }

}
