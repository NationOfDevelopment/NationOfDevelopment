package com.sparta.nationofdevelopment.domain.review.service;

import com.sparta.nationofdevelopment.common_entity.ErrorStatus;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.common.exception.ApiException;
import com.sparta.nationofdevelopment.domain.common.exception.InvalidRequestException;
import com.sparta.nationofdevelopment.domain.review.dto.request.ReviewRequestDto;
import com.sparta.nationofdevelopment.domain.review.dto.response.ReviewResponseDto;
import com.sparta.nationofdevelopment.domain.review.entity.Review;
import com.sparta.nationofdevelopment.domain.review.repository.ReviewRepository;
import com.sparta.nationofdevelopment.domain.user.entity.Users;
import com.sparta.nationofdevelopment.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Transactional
    public ReviewResponseDto saveReview(ReviewRequestDto requestDto, AuthUser authUser, Long orderId) {

        Users user = Users.fromAuthUser(authUser);

        // order의 주문자와 user가 동일한지 확인
        // order에서 store 정보를 꺼내 리뷰에 저장

        Review review = new Review(user, requestDto.getStars(), requestDto.getContents());
        Review saved = reviewRepository.save(review);
        return ReviewResponseDto.of(saved);
    }

    public List<ReviewResponseDto> getReviews(int min, int max, Long storeId) {
        List<Review> reviews;

        // 해당 store의 리뷰만 반환

        if (min == 1 && max == 5) {
            reviews = reviewRepository.findAllByOrderByCreatedAtDesc();
        } else {
            reviews = reviewRepository.findByStarsBetweenOrderByCreatedAtDesc(min, max);
        }
        return reviews.stream().map(ReviewResponseDto::of).toList();
    }
}
