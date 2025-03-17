package com.shose.shoseshop.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponse {
    private Long id;
    private String comment;
    private int rating;
    private LocalDateTime createdAt;
    private UserResponse user;
    private ProductResponse product;
}
