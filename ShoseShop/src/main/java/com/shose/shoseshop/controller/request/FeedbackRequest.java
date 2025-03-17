package com.shose.shoseshop.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRequest {

    private Long id;
    private String comment;
    private int rating;
    private Long userId;
    private Long productId;
}
