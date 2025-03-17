package com.shose.shoseshop.controller.response;

import com.shose.shoseshop.constant.NotificationType;

import java.time.LocalDateTime;

public class NotificationResponse {
    private Long id;
    private String message;
    private NotificationType type;
    private String data;
    private String title;
    private boolean isRead ;
    private LocalDateTime createdAt;
}
