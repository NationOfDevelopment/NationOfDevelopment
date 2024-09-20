package com.sparta.nationofdevelopment.domain.review.repository;

import com.sparta.nationofdevelopment.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByOrderByCreatedAtDesc();

    List<Review> findByStarsBetweenOrderByCreatedAtDesc(int min, int max);
}
