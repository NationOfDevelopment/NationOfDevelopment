package com.sparta.nationofdevelopment.domain.order.controller;

import com.sparta.nationofdevelopment.common_entity.ApiResponse;
import com.sparta.nationofdevelopment.domain.common.annotation.Auth;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.order.dto.OrderRequestDto;
import com.sparta.nationofdevelopment.domain.order.dto.OrderResponseDto;
import com.sparta.nationofdevelopment.domain.order.dto.OrderStatusRequestDto;
import com.sparta.nationofdevelopment.domain.order.dto.OrderStatusResponseDto;
import com.sparta.nationofdevelopment.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/stores/{store_id}/orders")
    public ApiResponse<OrderResponseDto> create(@Auth AuthUser authUser,
                                                @PathVariable long store_id,
                                                @Valid @RequestBody OrderRequestDto requestDto) {
        return ApiResponse.onSuccess(orderService.create(authUser, store_id, requestDto));
    }

    @PutMapping("/owner/orders/{order_id}")
    public ApiResponse<OrderStatusResponseDto> changeStatus(@Auth AuthUser authUser,
                                                            @PathVariable long order_id,
                                                            @Valid @RequestBody OrderStatusRequestDto requestDto) {
        return ApiResponse.onSuccess(orderService.changeStatus(authUser, order_id, requestDto));
    }
}
