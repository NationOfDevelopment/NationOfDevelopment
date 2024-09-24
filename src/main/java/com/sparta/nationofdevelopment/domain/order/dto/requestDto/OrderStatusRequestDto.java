package com.sparta.nationofdevelopment.domain.order.dto.requestDto;

import com.sparta.nationofdevelopment.domain.order.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusRequestDto {
    @NotNull
    private OrderStatus status;
}
