package com.sparta.nationofdevelopment.domain.order.controller;

import com.sparta.nationofdevelopment.common_entity.ApiResponse;
import com.sparta.nationofdevelopment.domain.common.annotation.Auth;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.order.dto.requestDto.OrderRequestDto;
import com.sparta.nationofdevelopment.domain.order.dto.responseDto.OrderResponseDto;
import com.sparta.nationofdevelopment.domain.order.dto.requestDto.OrderStatusRequestDto;
import com.sparta.nationofdevelopment.domain.order.dto.responseDto.OrderStatusResponseDto;
import com.sparta.nationofdevelopment.domain.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/stores/{storeId}/orders")
    public ApiResponse<OrderResponseDto> create(@Auth AuthUser authUser,
                                                @PathVariable long storeId,
                                                @Valid @RequestBody OrderRequestDto requestDto) {
        return ApiResponse.onSuccess(orderService.create(authUser, storeId, requestDto));
    }

    @PutMapping("/owner/orders/{orderId}")
    public ApiResponse<OrderStatusResponseDto> changeStatus(@Auth AuthUser authUser,
                                                            @PathVariable long orderId,
                                                            @Valid @RequestBody OrderStatusRequestDto requestDto) {
        return ApiResponse.onSuccess(orderService.changeStatus(authUser, orderId, requestDto));
    }

    @GetMapping("/stores/{storeId}/orders")
    public ApiResponse<List<OrderResponseDto>> findByStoreId(@PathVariable long storeId) {
        return ApiResponse.onSuccess(orderService.findByStoreId(storeId));
    }

    @GetMapping("/users/orders")
    public ApiResponse<List<OrderResponseDto>> findByUser(@Auth AuthUser authUser) {
        return ApiResponse.onSuccess(orderService.findByUser(authUser));
    }

    @GetMapping("/orders/{orderId}")
    public ApiResponse<OrderResponseDto> findByOrderId(@PathVariable long orderId) {
        return ApiResponse.onSuccess(orderService.findByOrderId(orderId));
    }
}
