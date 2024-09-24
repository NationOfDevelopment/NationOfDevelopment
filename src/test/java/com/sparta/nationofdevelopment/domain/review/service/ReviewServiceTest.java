package com.sparta.nationofdevelopment.domain.review.service;

import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.order.OrderStatus;
import com.sparta.nationofdevelopment.domain.order.entity.Orders;
import com.sparta.nationofdevelopment.domain.order.repository.OrderRepository;
import com.sparta.nationofdevelopment.domain.review.dto.request.ReviewRequestDto;
import com.sparta.nationofdevelopment.domain.review.dto.response.ReviewResponseDto;
import com.sparta.nationofdevelopment.domain.review.entity.Review;
import com.sparta.nationofdevelopment.domain.review.repository.ReviewRepository;
import com.sparta.nationofdevelopment.domain.store.entity.Store;
import com.sparta.nationofdevelopment.domain.store.repository.StoreRepository;
import com.sparta.nationofdevelopment.domain.user.entity.Users;
import com.sparta.nationofdevelopment.domain.user.enums.UserRole;
import com.sparta.nationofdevelopment.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ReviewService reviewService;

    @Test
    void review_save_successfully() {
        //given
        AuthUser authUser = new AuthUser(1L, "test@email.com", "userA", UserRole.USER);
        Users user = Users.fromAuthUser(authUser);
        ReviewRequestDto requestDto = new ReviewRequestDto(3, "good");
        Store store = new Store();
        ReflectionTestUtils.setField(store, "storeId", 1L);
        Long orderId = 1L;
        Orders order = new Orders(1, user, store, OrderStatus.ACCEPTED);
        Review review = new Review(user, store, order, 3, "good");

        given(userRepository.findByEmail(authUser.getEmail())).willReturn(Optional.of(user));
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
        given(reviewRepository.findReviewByOrder_Id(orderId)).willReturn(Optional.empty());
        given(reviewRepository.save(any(Review.class))).willReturn(review);

        // when
        ReviewResponseDto response = reviewService.saveReview(requestDto, authUser, orderId);

        // then
        assertNotNull(response);
    }


}