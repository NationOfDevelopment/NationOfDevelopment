package com.sparta.nationofdevelopment.domain.review.dto.response;

import com.sparta.nationofdevelopment.domain.menu.dto.MenuResponseDto;
import com.sparta.nationofdevelopment.domain.review.entity.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class ReviewResponseDto {
    private Long reviewId;
    private ReviewUserResponseDto user;
    private ReviewStoreResponseDto store;
    private ReviewOrderResponseDto order;
    private int stars;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReviewResponseDto of(Review review) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.reviewId = review.getReviewId();
        dto.stars = review.getStars();
        dto.contents = review.getContents();
        dto.createdAt = review.getCreatedAt();
        dto.updatedAt = review.getUpdatedAt();
        dto.user = ReviewUserResponseDto.of(review.getUser());
        dto.store = ReviewStoreResponseDto.of(review.getStore());
        dto.order = ReviewOrderResponseDto.of(review.getOrder());
        return dto;
    }
}
