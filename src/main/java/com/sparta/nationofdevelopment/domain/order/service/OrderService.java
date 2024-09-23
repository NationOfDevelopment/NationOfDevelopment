package com.sparta.nationofdevelopment.domain.order.service;

import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.common.exception.InvalidRequestException;
import com.sparta.nationofdevelopment.domain.menu.entity.Menu;
import com.sparta.nationofdevelopment.domain.menu.repository.MenuRepository;
import com.sparta.nationofdevelopment.domain.order.OrderStatus;
import com.sparta.nationofdevelopment.domain.order.dto.*;
import com.sparta.nationofdevelopment.domain.order.entity.Cart;
import com.sparta.nationofdevelopment.domain.order.entity.Orders;
import com.sparta.nationofdevelopment.domain.order.repository.OrderRepository;
import com.sparta.nationofdevelopment.domain.store.entity.Store;
import com.sparta.nationofdevelopment.domain.store.repository.StoreRepository;
import com.sparta.nationofdevelopment.domain.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;

    /**
     * OrderRequestDto에서 장바구니 리스트 반환
     */
    public List<Cart> convertToCartList(OrderRequestDto requestDto, AuthUser authUser) {
        List<Cart> cartList = new ArrayList<>();
        List<MenuItemDto> menuItemDtos = requestDto.getMenuItems();
        for (MenuItemDto dto : menuItemDtos) {
            Menu menu = menuRepository.findByMenuName(dto.getMenuName()).orElseThrow(() -> new InvalidRequestException("해당 메뉴는 존재하지 않습니다."));
            Cart cart = new Cart(dto, authUser, menu);
            cartList.add(cart);
        }
        return cartList;
    }

    /**
     * 주문 생성
     * 1. 주문 : 주문Id, 유저Id, 가게Id, 총 가격, 주문 시간 , status 주문 요청으로 변경
     */
    public OrderResponseDto create(AuthUser authUser, long storeId, OrderRequestDto requestDto) {
        Users currentUser = Users.fromAuthUser(authUser);
        Store foundStore = storeRepository.findById(storeId).orElseThrow(() -> new InvalidRequestException("해당 가게를 찾을 수 없습니다."));
        List<Cart> cartList = convertToCartList(requestDto, authUser);
        int totalAmount = 0;
        for (Cart cart : cartList) {
            totalAmount += cart.getAmount();
        }
        Orders order = new Orders(totalAmount, currentUser, foundStore, OrderStatus.WAITING);

        Orders savedOrder = orderRepository.save(order);

        return new OrderResponseDto(savedOrder, cartList);
    }

    /**
     * 주문 상태 변경
     * order_id, 순서 - > waiting -> ACCEPTED/REJECTED -> DELIVERED/CANCELLED
     */
    public OrderStatusResponseDto changeStatus(AuthUser authUser, long orderId, OrderStatusRequestDto requestDto) {
        Orders foundOrder = orderRepository.findById(orderId).orElseThrow(
                ()-> new InvalidRequestException("해당 주문을 찾을 수 없습니다.")
        );

        Users currentUser = Users.fromAuthUser(authUser);
        Users owner = foundOrder.getStore().getUser();

        if(!currentUser.getId().equals(owner.getId())) {
            throw new InvalidRequestException("해당 가게 사장님만 해당 주문을 관리할 수 있습니다.");
        }

        if((requestDto.getStatus().equals("ACCEPTED") || requestDto.getStatus().equals("REJECTED")) && !foundOrder.getStatus().equals(OrderStatus.WAITING)) {
            throw new InvalidRequestException("수락 대기 상태의 주문만 수락하거나 거절할 수 있습니다.");
        }

        if((requestDto.getStatus().equals("DELIVERED") || requestDto.getStatus().equals("CANCELLED")) && !foundOrder.getStatus().equals(OrderStatus.ACCEPTED)) {
            throw new InvalidRequestException("현재 진행중인 주문만 완료하거나 취소할 수 있습니다.");
        }

        foundOrder.setStatus(OrderStatus.valueOf(requestDto.getStatus()));

        Orders savedOrder = orderRepository.save(foundOrder);

        return new OrderStatusResponseDto(savedOrder);
    }


}
