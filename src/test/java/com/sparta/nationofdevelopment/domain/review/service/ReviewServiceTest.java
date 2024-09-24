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

import java.util.ArrayList;
import java.util.List;
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
        Long orderId = 1L;
        Orders order = new Orders(1000, user, store, OrderStatus.DELIVERED);
        Review review = new Review(user, store, order, 3, "good");

        given(userRepository.findByEmail(authUser.getEmail())).willReturn(Optional.of(user));
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
        given(reviewRepository.findReviewByOrder_Id(orderId)).willReturn(Optional.empty());
        given(reviewRepository.save(any(Review.class))).willReturn(review);

        // when
        ReviewResponseDto response = reviewService.saveReview(requestDto, authUser, orderId);

        // then
        assertNotNull(response);
        assertEquals(response.getContents(), "good");
        assertEquals(response.getStars(), 3);
    }

    @Test
    void try_duplicated_review() {
        //given
        AuthUser authUser = new AuthUser(1L, "test@email.com", "userA", UserRole.USER);
        Users user = Users.fromAuthUser(authUser);
        ReviewRequestDto requestDto = new ReviewRequestDto(3, "good");
        Store store = new Store();
        Long orderId = 1L;
        Orders order = new Orders(1000, user, store, OrderStatus.DELIVERED);

        given(userRepository.findByEmail(authUser.getEmail())).willReturn(Optional.of(user));
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));
        given(reviewRepository.findReviewByOrder_Id(orderId)).willReturn(Optional.of(new Review()));

        // when & then
        ApiException exception = assertThrows(ApiException.class, () ->
                reviewService.saveReview(requestDto, authUser, orderId)
        );
        assertEquals(ErrorStatus._BAD_REQUEST_DUP_REVIEW, exception.getErrorCode());
    }

    @Test
    void review_another_order() {
        //given
        AuthUser authUser = new AuthUser(1L, "test@email.com", "userA", UserRole.USER);
        Users user = Users.fromAuthUser(authUser);
        ReviewRequestDto requestDto = new ReviewRequestDto(3, "good");
        Store store = new Store();
        Long orderId = 1L;
        Orders order = new Orders(1000, new Users(2L, "test2@email.com", "userB", UserRole.USER), store, OrderStatus.DELIVERED);

        given(userRepository.findByEmail(authUser.getEmail())).willReturn(Optional.of(user));
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

        // when & then
        ApiException exception = assertThrows(ApiException.class, () ->
                reviewService.saveReview(requestDto, authUser, orderId)
        );
        assertEquals(ErrorStatus._BAD_REQUEST_NOT_ORDERER, exception.getErrorCode());
    }

    @Test
    void review_deleted_user() {
        //given
        AuthUser authUser = new AuthUser(1L, "test@email.com", "userA", UserRole.USER);
        Users user = Users.fromAuthUser(authUser);
        ReviewRequestDto requestDto = new ReviewRequestDto(3, "good");
        Store store = new Store();
        Long orderId = 1L;
        Orders order = new Orders(1000, user, store, OrderStatus.DELIVERED);
        ReflectionTestUtils.setField(user, "isDeleted", true);

        given(userRepository.findByEmail(authUser.getEmail())).willReturn(Optional.of(user));

        // when & then
        ApiException exception = assertThrows(ApiException.class, () ->
                reviewService.saveReview(requestDto, authUser, orderId)
        );
        assertEquals(ErrorStatus._DELETED_USER, exception.getErrorCode());
    }

    @Test
    void review_not_delivered_order() {
        //given
        AuthUser authUser = new AuthUser(1L, "test@email.com", "userA", UserRole.USER);
        Users user = Users.fromAuthUser(authUser);
        ReviewRequestDto requestDto = new ReviewRequestDto(3, "good");
        Store store = new Store();
        Long orderId = 1L;
        Orders order = new Orders(1000, user, store, OrderStatus.CANCELLED);

        given(userRepository.findByEmail(authUser.getEmail())).willReturn(Optional.of(user));
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

        // when & then
        ApiException exception = assertThrows(ApiException.class, () ->
                reviewService.saveReview(requestDto, authUser, orderId)
        );
        assertEquals(ErrorStatus._BAD_REQUEST_ORDER_STATUS, exception.getErrorCode());
    }

    @Test
    void get_review_successfully() {
        // given
        Users user = new Users(1L, "test@email.com", "userA", UserRole.USER);
        Store store = new Store();
        Orders order1 = new Orders(1000, user, store, OrderStatus.DELIVERED);
        Orders order2 = new Orders(2000, user, store, OrderStatus.DELIVERED);
        Review review1 = new Review(user, store, order1, 3, "good");
        Review review2 = new Review(user, store, order2, 5, "wow");
        ArrayList<Review> list = new ArrayList<>();
        list.add(review1);
        list.add(review2);

        given(storeRepository.findById(any())).willReturn(Optional.of(store));
        given(reviewRepository.findByStore_StoreIdAndStarsBetweenOrderByCreatedAtDesc(1L, 1, 5))
                .willReturn(list);

        // when
        List<ReviewResponseDto> reviews = reviewService.getReviews(1, 5, 1L);

        // then
        assertEquals(reviews.size(), 2);
    }

    @Test
    void get_review_store_not_exists() {
        // given
        given(storeRepository.findById(any())).willReturn(Optional.empty());

        // when & then
        ApiException exception = assertThrows(ApiException.class, () ->
                reviewService.getReviews(1, 5, 1L)
        );
        assertEquals(ErrorStatus._NOT_FOUND_STORE, exception.getErrorCode());
    }


}