package com.shose.shoseshop.repository;

import com.shose.shoseshop.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    Optional<Page<Feedback>> findByProductId(Long productId, Pageable pageable);
    Optional<List<Feedback>> findByProductId(Long productId);
    Optional<Feedback> findByUserIdAndProductId(Long userId, Long productId);
}
