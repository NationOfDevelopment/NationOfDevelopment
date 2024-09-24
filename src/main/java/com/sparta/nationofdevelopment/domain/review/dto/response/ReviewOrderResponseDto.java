package com.sparta.nationofdevelopment.domain.review.dto.response;

import com.sparta.nationofdevelopment.domain.order.OrderStatus;
import com.sparta.nationofdevelopment.domain.order.entity.Orders;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewOrderResponseDto {
    private Long orderId;
    private OrderStatus status;
    private int totalAmount;

    public static ReviewOrderResponseDto of(Orders order) {
        ReviewOrderResponseDto responseDto = new ReviewOrderResponseDto();
        responseDto.orderId = order.getId();
        responseDto.status = order.getStatus();
        responseDto.totalAmount = order.getTotalAmount();
        return responseDto;
    }
}
