package com.example.car_rental.controller;

import com.example.car_rental.dto.responseDTO.ApiResponse;
import com.example.car_rental.dto.responseDTO.InvoicesResponse;
import com.example.car_rental.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invoices")
public class InvoiceController {

    final private InvoiceService invoiceService;

    //ADMIN
    @GetMapping("/{bookingId}")
    public ApiResponse<InvoicesResponse> getInvoices(@PathVariable(value = "bookingId") Long bookingId) {

        return ApiResponse.<InvoicesResponse>builder()
                .code(200)
                .data(invoiceService.getInvoiceByBookingId(bookingId))
                .build();
    }

    //USER
    @GetMapping("/me/{bookingId}")
    public ApiResponse<InvoicesResponse> getInvoiceUser(@PathVariable(value = "bookingId") Long bookingId) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return ApiResponse.<InvoicesResponse>builder()
                .code(200)
                .data(invoiceService.getInvoiceByBookingIdUser(bookingId, email))
                .build();
    }

}
