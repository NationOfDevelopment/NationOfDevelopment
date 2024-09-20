package com.sparta.nationofdevelopment.domain.review.service;

import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.common.exception.InvalidRequestException;
import com.sparta.nationofdevelopment.domain.review.dto.request.ReviewRequestDto;
import com.sparta.nationofdevelopment.domain.review.dto.response.ReviewResponseDto;
import com.sparta.nationofdevelopment.domain.review.entity.Review;
import com.sparta.nationofdevelopment.domain.review.repository.ReviewRepository;
import com.sparta.nationofdevelopment.domain.user.entity.Users;
import com.sparta.nationofdevelopment.domain.user.enums.UserRole;
import com.sparta.nationofdevelopment.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewResponseDto saveReview(ReviewRequestDto requestDto, AuthUser authUser) {
        Users user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(() -> new InvalidRequestException("존재하지 않는 유저입니다."));

        Review review = new Review(user, requestDto.getStars(), requestDto.getContents());
        Review saved = reviewRepository.save(review);
        return ReviewResponseDto.of(saved);
    }

    public List<ReviewResponseDto> getReviews(int min, int max) {
        List<Review> reviews;
        if (min == 1 && max == 5) {
            reviews = reviewRepository.findAllByOrderByCreatedAtDesc();
        } else {
            reviews = reviewRepository.findByStarsBetweenOrderByCreatedAtDesc(min, max);
        }
        return reviews.stream().map(ReviewResponseDto::of).toList();
    }
}
