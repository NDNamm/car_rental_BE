package com.example.car_rental.service;

import com.example.car_rental.dto.requestDTO.CategoryRequest;
import com.example.car_rental.dto.requestDTO.RefundRequest;
import com.example.car_rental.dto.responseDTO.CategoryResponse;
import com.example.car_rental.dto.responseDTO.RefundResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface RefundService {
    //USER
    RefundResponse getRefundUser(String email,Long refundId);
    RefundResponse cancelRefund( Long bookingId , RefundRequest request, MultipartFile qrCode);

    //ADMIN
    RefundResponse updateStatus(Long refundId , RefundRequest request, MultipartFile adminProofImage );
    Page<RefundResponse> getAllRefund(String bookingCode, String status, int page, int size);
}
