package com.sparta.nationofdevelopment.domain.order.service;

import com.sparta.nationofdevelopment.common_entity.ErrorStatus;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.common.exception.ApiException;
import com.sparta.nationofdevelopment.domain.common.exception.InvalidRequestException;
import com.sparta.nationofdevelopment.domain.order.OrderStatus;
import com.sparta.nationofdevelopment.domain.order.dto.*;
import com.sparta.nationofdevelopment.domain.order.entity.Cart;
import com.sparta.nationofdevelopment.domain.order.entity.Orders;
import com.sparta.nationofdevelopment.domain.order.repository.CartRepository;
import com.sparta.nationofdevelopment.domain.order.repository.OrderRepository;
import com.sparta.nationofdevelopment.domain.store.entity.Store;
import com.sparta.nationofdevelopment.domain.store.repository.StoreRepository;
import com.sparta.nationofdevelopment.domain.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;

    /**
     * 주문 생성
     * 1. 주문 : 주문Id, 유저Id, 가게Id, 총 가격, 주문 시간 , status 주문 요청으로 변경
     */
    @Transactional
    public OrderResponseDto create(AuthUser authUser, long storeId, OrderRequestDto requestDto) {
        Users currentUser = Users.fromAuthUser(authUser);
        Store foundStore = storeRepository.findById(storeId).orElseThrow(() -> new InvalidRequestException("해당 가게를 찾을 수 없습니다."));
        List<Cart> cartList = cartService.convertToCartList(requestDto, authUser);
        int totalAmount = 0;
        for (Cart cart : cartList) {
            totalAmount += cart.getAmount();
        }
        Orders order = new Orders(totalAmount, currentUser, foundStore, OrderStatus.WAITING);

        Orders savedOrder = orderRepository.save(order);
        for(Cart cart : cartList) {
            cart.setOrderId(savedOrder.getId());
        }

        List<CartDto> cartDtos = cartList.stream().map(CartDto::new).toList();

        return new OrderResponseDto(savedOrder, cartDtos);
    }

    /**
     * 주문 상태 변경
     * order_id, 순서 - > waiting -> ACCEPTED/REJECTED -> DELIVERED/CANCELLED
     */
    @Transactional
    public OrderStatusResponseDto changeStatus(AuthUser authUser, long orderId, OrderStatusRequestDto requestDto) {
        Orders foundOrder = orderRepository.findById(orderId).orElseThrow(
                ()-> new ApiException(ErrorStatus._BAD_REQUEST_NOT_FOUND_ORDER)
        );

        Users currentUser = Users.fromAuthUser(authUser);
        Users owner = foundOrder.getStore().getUser();

        if(!currentUser.getId().equals(owner.getId())) {
            throw new ApiException(ErrorStatus._FORBIDDEN_NO_AUTHORITY_MANAGE_ORDER);
        }

        if(requestDto.getStatus().equals("WAITING")) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_CAN_NOT_CHANGE_TO_WAITING);
        }

        if((requestDto.getStatus().equals("ACCEPTED") || requestDto.getStatus().equals("REJECTED")) && !foundOrder.getStatus().equals(OrderStatus.WAITING)) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_INVALID_STATUS_ACCEPTED_OR_REJECTED);
        }

        if((requestDto.getStatus().equals("DELIVERED") || requestDto.getStatus().equals("CANCELLED")) && !foundOrder.getStatus().equals(OrderStatus.ACCEPTED)) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_INVALID_STATUS_INVALID_ORDER);
        }

        foundOrder.setStatus(requestDto.getStatus());

        Orders savedOrder = orderRepository.save(foundOrder);

        return new OrderStatusResponseDto(savedOrder);
    }

    //OrderList에 CartDto를 담아서 OrderResponseDto로 반환
    public List<OrderResponseDto> convertToOrderResponseDtos(List<Orders> ordersList) {
        return ordersList.stream()
                .map(order -> {
                    List<Cart> cartList = cartRepository.findByOrderId(order.getId());

                    List<CartDto> cartDtos = cartList.stream()
                            .map(CartDto::new)
                            .toList();

                    return new OrderResponseDto(order, cartDtos);
                })
                .toList();
    }

    //가게 별 주문 전체 조회
    public List<OrderResponseDto> findByStoreId(long storeId) {
        List<Orders> ordersList = orderRepository.findByStore_StoreId(storeId);
        return convertToOrderResponseDtos(ordersList);
    }

    //유저 별 주문 조회
    public List<OrderResponseDto> findByUser(AuthUser authUser) {
        long userId = authUser.getId();
        List<Orders> ordersList = orderRepository.findByUserId(userId);
        return convertToOrderResponseDtos(ordersList);
    }

    //주문 단건 조회
    public OrderResponseDto findByOrderId(long orderId) {
        Orders foundOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ApiException(ErrorStatus._BAD_REQUEST_NOT_FOUND_ORDER));
        List<Cart> foundCarts = cartRepository.findByOrderId(orderId);
        List<CartDto> cartDtoList = foundCarts.stream().map(CartDto::new).toList();
        return new OrderResponseDto(foundOrder,cartDtoList);
    }
}
