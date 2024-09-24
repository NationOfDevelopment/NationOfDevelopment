package com.sparta.nationofdevelopment.domain.review.repository;

import com.sparta.nationofdevelopment.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByStore_StoreIdOrderByCreatedAtDesc(Long storeId);

    List<Review> findByStore_StoreIdAndStarsBetweenOrderByCreatedAtDesc(Long storeId, int min, int max);

    Optional<Review> findReviewByOrder_Id(Long orderId);
}
