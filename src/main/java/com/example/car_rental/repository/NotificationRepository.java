package com.example.car_rental.repository;

import com.example.car_rental.entity.Notifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notifications, Long> {
    @Modifying
    @Query("""
                UPDATE Notifications n
                SET n.isRead = true
                WHERE n.user.id = :userId
                  AND n.isRead = false
            """)
    void markAllAsRead(@Param("userId") Long userId);

    @Modifying
    @Query("""
    UPDATE Notifications n
    SET n.isRead = true
    WHERE n.id = :notificationId
      AND n.user.id = :userId
""")
    void markAsRead(
            @Param("notificationId") Long notificationId,
            @Param("userId") Long userId
    );

    @Modifying
    @Query("""
                DELETE FROM Notifications n
                WHERE n.user.id = :userId
                    AND n.isRead = true
            """)
    void deleteAllRead(@Param("userId") Long userId);

    @Modifying
    @Query("""
                DELETE FROM Notifications n
                WHERE n.user.id = :userId
                And n.id = :notificationId
            """)
    void deleteNotification(@Param("notificationId") Long notificationId,
                    @Param("userId") Long userId);

    @Query("""
                SELECT COUNT(n) FROM Notifications n
                WHERE n.user.id = :userId
                  AND n.isRead = false
            """)
    Long countUnreadNotifications(@Param("userId") Long userId);

    Page<Notifications> findByUserId(Long userId, Pageable pageable);

}
