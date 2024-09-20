package com.sparta.nationofdevelopment.domain.review.dto.response;

import com.sparta.nationofdevelopment.domain.review.entity.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class ReviewResponseDto {
    private Long reviewId;
//    private UserResponseDto user;
//    private StoreResponseDto store;
//    private MenuResponseDto menu;
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
        return dto;
    }
}
