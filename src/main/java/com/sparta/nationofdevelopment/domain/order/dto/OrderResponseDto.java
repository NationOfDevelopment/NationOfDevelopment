package com.sparta.nationofdevelopment.domain.order.dto;

import com.sparta.nationofdevelopment.domain.order.OrderStatus;
import com.sparta.nationofdevelopment.domain.order.entity.Cart;
import com.sparta.nationofdevelopment.domain.order.entity.Order;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderResponseDto {
    private final long orderId;
    private final long userId;
    private final long storeId;
    private final int totalAmount;
    private final OrderStatus status;
    private final LocalDateTime orderDate;
    private final List<Cart> cartList;

    public OrderResponseDto(Order order , List<Cart> cartList) {
        this.orderId = order.getId();
        this.userId = order.getUser().getId();
        this.storeId = order.getStore().getId();
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus();
        this.orderDate = order.getCreatedAt();
        this.cartList = cartList;
    }

}
