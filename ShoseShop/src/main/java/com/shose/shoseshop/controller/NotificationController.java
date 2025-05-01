package com.shose.shoseshop.controller;

import com.shose.shoseshop.controller.response.NotificationResponse;
import com.shose.shoseshop.controller.response.ResponseData;
import com.shose.shoseshop.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class NotificationController {

    NotificationService notificationService;

    @GetMapping
    public ResponseData<List<NotificationResponse>> getNotifications() {
        List<NotificationResponse> notifications = notificationService.getNotifications();
        return new ResponseData<>("Get notifications successfully", notifications);
    }

    @PutMapping("/{id}/read")
    public ResponseData<?> readNotification(@PathVariable("id") Long id) {
        notificationService.makeAsRead(id);
        return new ResponseData<>(id);
    }

    @GetMapping("/unread-count")
    public ResponseData<Integer> getUnreadNotificationCount() {
        int count = notificationService.getUnreadNotificationCount();
        return new ResponseData<>("Get unread notification count successfully", count);
    }
}
