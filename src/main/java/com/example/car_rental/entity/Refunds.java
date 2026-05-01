package com.example.car_rental.entity;

import com.example.car_rental.entity.enums.RefundStatus;
import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refunds")
public class Refunds {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_code", unique = true, length = 20)
    private String bookingCode;

    @Column(name = "reason")
    private String reason;

    @Column(name = "amount", precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "hours_before")
    private Long hoursBefore;

    @Column(name = "refund_percent")
    private int refundPercent;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RefundStatus status;

    @Column(name = "public_id")
    private String publicId;

    @Column(name = "qr_code", unique = true)
    private String qrCodeUrl;

    @Column(name = "note")
    private String note;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "public_id_proof")
    private String publicIdProof;

    @Column(name = "admin_proof_image", unique = true)
    private String adminProofImage;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Bookings booking;
}
