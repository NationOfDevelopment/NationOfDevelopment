package com.sparta.nationofdevelopment.domain.order.dto;

import com.sparta.nationofdevelopment.domain.order.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderStatusResponseDto {
    private long orderId;
    private OrderStatus status;
    private LocalDateTime modifiedAt;
}
