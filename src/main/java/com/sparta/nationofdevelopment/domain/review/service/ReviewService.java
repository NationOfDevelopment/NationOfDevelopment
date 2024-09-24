package com.sparta.nationofdevelopment.domain.review.service;

import com.sparta.nationofdevelopment.common_entity.ErrorStatus;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.common.exception.ApiException;
import com.sparta.nationofdevelopment.domain.order.OrderStatus;
import com.sparta.nationofdevelopment.domain.order.entity.Orders;
import com.sparta.nationofdevelopment.domain.order.repository.OrderRepository;
import com.sparta.nationofdevelopment.domain.review.dto.request.ReviewRequestDto;
import com.sparta.nationofdevelopment.domain.review.dto.response.ReviewResponseDto;
import com.sparta.nationofdevelopment.domain.review.entity.Review;
import com.sparta.nationofdevelopment.domain.review.repository.ReviewRepository;
import com.sparta.nationofdevelopment.domain.store.repository.StoreRepository;
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
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public ReviewResponseDto saveReview(ReviewRequestDto requestDto, AuthUser authUser, Long orderId) {

        Users user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_EMAIL));

        if (user.getIsDeleted()) {
            throw new ApiException(ErrorStatus._DELETED_USER);
        }

        Orders order = validationOrder(orderId, user);

        // order에서 store 정보를 꺼내 리뷰에 저장
        Review review = new Review(user, order.getStore(), order, requestDto.getStars(), requestDto.getContents());
        Review saved = reviewRepository.save(review);
        return ReviewResponseDto.of(saved);
    }

    private Orders validationOrder(Long orderId, Users user) {
        // order의 주문자와 user가 동일한지 확인
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(ErrorStatus._BAD_REQUEST_NOT_FOUND_ORDER));

        if (order.getUser() != user) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_NOT_ORDERER);
        }
        // 완료된 주문만 리뷰 작성 가능
        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_ORDER_STATUS);
        }
        // 이미 리뷰를 작성한 경우
        reviewRepository.findReviewByOrder_Id(orderId).ifPresent((r) -> {throw new ApiException(ErrorStatus._BAD_REQUEST_DUP_REVIEW);});
        return order;
    }

    public List<ReviewResponseDto> getReviews(int min, int max, Long storeId) {
        storeRepository.findById(storeId).orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_STORE));
        return reviewRepository.findByStore_StoreIdAndStarsBetweenOrderByCreatedAtDesc(storeId, min, max).stream().map(ReviewResponseDto::of).toList();
    }
}
