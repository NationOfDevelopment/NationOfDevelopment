package com.sparta.nationofdevelopment.domain.order.dto;

import com.sparta.nationofdevelopment.domain.order.OrderStatus;
import com.sparta.nationofdevelopment.domain.order.entity.Orders;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderStatusResponseDto {
    private long orderId;
    private OrderStatus status;
    private LocalDateTime updatedAt;

    public OrderStatusResponseDto(Orders order) {
        this.orderId = order.getId();
        this.status = order.getStatus();
        this.updatedAt = order.getUpdatedAt();
    }
}
