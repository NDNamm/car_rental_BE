package com.example.car_rental.repository;

import com.example.car_rental.entity.Bookings;
import com.example.car_rental.entity.Reviews;
import com.example.car_rental.entity.Roles;
import com.example.car_rental.entity.enums.BookingStatusEnum;
import com.example.car_rental.entity.enums.ReviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Reviews, Long> {
    boolean existsByBookingId(Long bookingId);
    @Query("""
                SELECT r FROM Reviews r WHERE r.user.email = :email
                  AND (:status IS NULL OR LOWER(r.status) LIKE LOWER(CONCAT('%', :status, '%')))
            """)
    Page<Reviews> findReviewByUser(
            @Param("email") String email,
            @Param("status") ReviewStatus status,
            Pageable pageable);


    @Query("SELECT r FROM Reviews r WHERE r.car.id = :carId AND r.status = :status")
    List<Reviews> findApprovedReviewsByCarId(Long carId, ReviewStatus status);

    @Query("""
    SELECT r FROM Reviews r 
    WHERE (:status IS NULL OR r.status = :status)
""")
    Page<Reviews> findAllReview(
            @Param("status") ReviewStatus status,
            Pageable pageable);

    @Query("""
    SELECT r.booking.id
    FROM Reviews r
    WHERE r.booking.id IN :bookingIds
""")
    List<Long> findBookingIdsHaveReview(@Param("bookingIds") List<Long> bookingIds);

    @Query("""
    SELECT AVG(r.rating), COUNT(r.id)
    FROM Reviews r
    WHERE r.car.id = :carId
      AND r.status = 'APPROVED'
""")
    Object getRatingStats(@Param("carId") Long carId);
}
