package com.shose.shoseshop.service;

import com.shose.shoseshop.controller.request.FeedbackRequest;
import com.shose.shoseshop.controller.response.FeedbackResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedBackService {

    FeedbackResponse createFeedBack (FeedbackRequest feedbackRequest);

    Page<FeedbackResponse> getReviewByProductId(Pageable pageable, Long productId);

    FeedbackResponse getReviewByUserAndProduct(Long userId, Long productId);
}
