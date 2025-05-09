package com.shose.shoseshop.controller.response;

import com.shose.shoseshop.constant.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private Long id;
    private String message;
    private NotificationType type;
    private String data;
    private String title;
    private boolean isRead ;
    private LocalDateTime createdAt;
}
