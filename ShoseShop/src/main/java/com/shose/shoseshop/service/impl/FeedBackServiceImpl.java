package com.shose.shoseshop.service.impl;

import com.shose.shoseshop.configuration.CustomUserDetails;
import com.shose.shoseshop.controller.request.FeedbackRequest;
import com.shose.shoseshop.controller.response.FeedbackResponse;
import com.shose.shoseshop.entity.Feedback;
import com.shose.shoseshop.entity.Product;
import com.shose.shoseshop.entity.User;
import com.shose.shoseshop.exception.AppException;
import com.shose.shoseshop.exception.ErrorResponse;
import com.shose.shoseshop.repository.FeedbackRepository;
import com.shose.shoseshop.repository.ProductRepository;
import com.shose.shoseshop.repository.UserRepository;
import com.shose.shoseshop.service.FeedBackService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FeedBackServiceImpl implements FeedBackService {

    private final FeedbackRepository feedbackRepository;
    FeedbackRepository FeedbackRepository;
    UserRepository userRepository;
    ProductRepository productRepository;
    ModelMapper modelMapper;



    @Override
    public FeedbackResponse createFeedBack(FeedbackRequest feedbackRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info(username);
      //  User user = userRepository.findByUsername(username).orElseThrow(EntityNotFoundException::new);
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorResponse.USER_NOT_EXISTED)
        );
        Product product = productRepository.findById(feedbackRequest.getProductId()).orElseThrow(
                () -> new AppException(ErrorResponse.PRODUCT_NOT_EXISTED)
        );
        Feedback review = modelMapper.map(feedbackRequest, Feedback.class);
        review.setRate(feedbackRequest.getRating());
        review.setUser(user);
        review.setProduct(product);
        //review.setCreatedAt(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        FeedbackRepository.save(review);
        product.setStar(getRatingByProductId(product.getId()));
        productRepository.save(product);
        return modelMapper.map(review, FeedbackResponse.class);
    }

    @Override
    public Page<FeedbackResponse> getReviewByProductId(Pageable pageable, Long productId) {
        Page<Feedback> reviews = feedbackRepository.findByProductId(productId, pageable).orElseThrow(
                () -> new AppException(ErrorResponse.PRODUCT_NOT_EXISTED));
        return reviews.map(review -> modelMapper.map(review, FeedbackResponse.class));
    }

    @Override
    public FeedbackResponse getReviewByUserAndProduct(Long userId, Long productId) {
        Feedback review = feedbackRepository.findByUserIdAndProductId(userId, productId).orElseThrow(null);
        return modelMapper.map(review, FeedbackResponse.class);
    }

    public Float getRatingByProductId(Long productId) {
        List<Feedback> reviews = FeedbackRepository.findByProductId(productId).orElseThrow(
                () -> new AppException(ErrorResponse.PRODUCT_NOT_EXISTED));
        // lấy ra những review có rating khác 0, sau đó tính trung bình
        return (float) reviews.stream().filter(review -> review.getRate() != 0)
                .mapToInt(Feedback::getRate).average().orElse(0);
    }
}
