package com.shose.shoseshop.repository;

import com.shose.shoseshop.entity.Notification;
import com.shose.shoseshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(User user);
    Integer countByUserAndIsReadFalse(User user);
}
