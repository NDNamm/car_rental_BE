package com.example.car_rental.repository;

import com.example.car_rental.entity.Reviews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Reviews, Long> {

    boolean existsByBookingId(Long bookingId);

    @Query("""
            SELECT r
            FROM Reviews r
            WHERE r.user.email = :email
              AND (:isHidden IS NULL OR r.isHidden = :isHidden)
            """)
    Page<Reviews> findReviewByUser(
            @Param("email") String email,
            @Param("isHidden") Boolean isHidden,
            Pageable pageable
    );

    @Query("""
    SELECT r
    FROM Reviews r
    WHERE r.car.id = :carId
      AND r.isHidden = false
    ORDER BY r.createdAt DESC
""")
    List<Reviews> findVisibleReviewsByCarId(
            @Param("carId") Long carId
    );

    @Query("""
            SELECT r
            FROM Reviews r
            WHERE (:isHidden IS NULL OR r.isHidden = :isHidden)
            """)
    Page<Reviews> findAllReview(
            @Param("isHidden") Boolean isHidden,
            Pageable pageable
    );

    @Query("""
            SELECT r.booking.id
            FROM Reviews r
            WHERE r.booking.id IN :bookingIds
            """)
    List<Long> findBookingIdsHaveReview(
            @Param("bookingIds") List<Long> bookingIds
    );

    @Query("""
            SELECT AVG(r.rating), COUNT(r.id)
            FROM Reviews r
            WHERE r.car.id = :carId
              AND r.isHidden = false
            """)
    Object getRatingStats(
            @Param("carId") Long carId
    );
}