package com.sparta.nationofdevelopment.domain.review.controller;

import com.sparta.nationofdevelopment.common_entity.ApiResponse;
import com.sparta.nationofdevelopment.domain.common.annotation.Auth;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.review.dto.request.ReviewRequestDto;
import com.sparta.nationofdevelopment.domain.review.dto.response.ReviewResponseDto;
import com.sparta.nationofdevelopment.domain.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/orders/{orderId}/reviews")
    public ApiResponse<ReviewResponseDto> postReview(@Auth AuthUser authUser,
                                        @Valid @RequestBody ReviewRequestDto requestDto,
                                        @PathVariable Long orderId) {
        return ApiResponse.onSuccess(reviewService.saveReview(requestDto, authUser));
    }

    @GetMapping("/stores/{storeId}/reviews")
    public ApiResponse<List<ReviewResponseDto>> getReviews(@PathVariable Long storeId,
                                              @RequestParam(defaultValue = "1") int min,
                                              @RequestParam(defaultValue = "5") int max) {
        return ApiResponse.onSuccess(reviewService.getReviews(min, max));
    }
}
