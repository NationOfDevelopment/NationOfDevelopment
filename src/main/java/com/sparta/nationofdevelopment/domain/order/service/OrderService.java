package com.sparta.nationofdevelopment.domain.order.service;

import com.sparta.nationofdevelopment.common_entity.ErrorStatus;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.common.exception.ApiException;
import com.sparta.nationofdevelopment.domain.common.module.Finder;
import com.sparta.nationofdevelopment.domain.order.OrderStatus;
import com.sparta.nationofdevelopment.domain.order.dto.requestDto.OrderStatusRequestDto;
import com.sparta.nationofdevelopment.domain.order.dto.responseDto.CartDto;
import com.sparta.nationofdevelopment.domain.order.dto.responseDto.OrderResponseDto;
import com.sparta.nationofdevelopment.domain.order.dto.responseDto.OrderStatusResponseDto;
import com.sparta.nationofdevelopment.domain.order.entity.Cart;
import com.sparta.nationofdevelopment.domain.order.entity.Orders;
import com.sparta.nationofdevelopment.domain.order.repository.CartRepository;
import com.sparta.nationofdevelopment.domain.order.repository.OrderRepository;
import com.sparta.nationofdevelopment.domain.store.entity.Store;
import com.sparta.nationofdevelopment.domain.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final Finder finder;
    private final Clock clock;

    /**
     * 주문 생성
     * 1. 주문 : 주문Id, 유저Id, 가게Id, 총 가격, 주문 시간 , status 주문 요청으로 변경
     */
    @Transactional
    public OrderResponseDto create(AuthUser authUser, long storeId) {
        Users currentUser = Users.fromAuthUser(authUser);
        Store foundStore = finder.findStoreByStoreId(storeId);
        validateIsStoreOpen(foundStore);

        List<Cart> cartList = cartRepository.findByUserIdAndOrderIdIsZero(currentUser.getId());

        int totalAmount = calculateTotalAmount(cartList);
        validateMinOrderAmount(foundStore,totalAmount);

        Orders savedOrder = orderRepository.save(
                new Orders(totalAmount, currentUser, foundStore, OrderStatus.WAITING));
        cartService.saveOrderIdsToCarts(cartList,savedOrder);

        List<CartDto> cartDtoList = cartList.stream().map(CartDto::new).toList();

        return new OrderResponseDto(savedOrder, cartDtoList);
    }

    //총 금액 계산 로직
    public int calculateTotalAmount(List<Cart> cartList) {
        int totalAmount = 0;
        for (Cart cart : cartList) {
            totalAmount += cart.getAmount();
        }
        return totalAmount;
    }

    //가게에서 설정한 최소 금액을 넘는지 검증
    public void validateMinOrderAmount(Store foundStore, int totalAmount) {
        if(foundStore.getMinOrderMount() > totalAmount) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_MIN_ORDER_AMOUNT);
        }
    }

    //주문 넣는 시간이 가게 영업시간인지 검증
    public void validateIsStoreOpen(Store store) {
        LocalTime now = LocalTime.now(clock);
        if(now.isBefore(store.getOpenTime()) || now.isAfter(store.getCloseTime())) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_STORE_CLOSED);
        }
    }

    /**
     * 주문 상태 변경
     * order_id, 순서 - > waiting -> ACCEPTED/REJECTED -> DELIVERED/CANCELLED
     */
    @Transactional
    public OrderStatusResponseDto changeStatus(AuthUser authUser, long orderId, OrderStatusRequestDto requestDto) {
        Orders foundOrder = finder.findOrderByOrderId(orderId);

        validateChangingOrderStatus(authUser,requestDto,foundOrder);

        foundOrder.setStatus(requestDto.getStatus());

        Orders savedOrder = orderRepository.save(foundOrder);

        return new OrderStatusResponseDto(savedOrder);
    }

    //주문 상태 검증
    public void validateChangingOrderStatus(AuthUser authUser,
                                            OrderStatusRequestDto requestDto,
                                            Orders foundOrder) {
        Users currentUser = Users.fromAuthUser(authUser);
        Users owner = foundOrder.getStore().getUser();

        if (!currentUser.getId().equals(owner.getId())) {
            throw new ApiException(ErrorStatus._FORBIDDEN_NO_AUTHORITY_MANAGE_ORDER);
        }

        if (requestDto.getStatus() == OrderStatus.WAITING) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_CAN_NOT_CHANGE_TO_WAITING);
        }

        if ((requestDto.getStatus()==OrderStatus.ACCEPTED || requestDto.getStatus() == OrderStatus.REJECTED) && foundOrder.getStatus() != OrderStatus.WAITING){
            throw new ApiException(ErrorStatus._BAD_REQUEST_INVALID_STATUS_ACCEPTED_OR_REJECTED);
        }

        if ((requestDto.getStatus()==OrderStatus.DELIVERED || requestDto.getStatus() == OrderStatus.CANCELLED) && foundOrder.getStatus() != OrderStatus.ACCEPTED) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_INVALID_STATUS_INVALID_ORDER);
        }
    }


    //OrderList에 CartDto를 담아서 OrderResponseDto로 반환
    public List<OrderResponseDto> convertToOrderResponseDtos(List<Orders> ordersList) {
        List<Long> orderIds = ordersList.stream().map(Orders::getId).toList();
        List<CartDto> cartDtos = cartRepository.findCartsByOrderIds(orderIds).stream().map(CartDto::new).toList();

        Map<Long,List<CartDto>> cartDtosMap = cartDtos.stream().collect(Collectors.groupingBy(CartDto::getOrderId));

        return ordersList.stream()
                .map(order -> new OrderResponseDto(order,cartDtosMap.get(order.getId()))).toList();
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
        Orders foundOrder = finder.findOrderByOrderId(orderId);
        List<Cart> foundCarts = cartRepository.findByOrderId(orderId);
        List<CartDto> cartDtoList = foundCarts.stream().map(CartDto::new).toList();
        return new OrderResponseDto(foundOrder, cartDtoList);
    }
}
