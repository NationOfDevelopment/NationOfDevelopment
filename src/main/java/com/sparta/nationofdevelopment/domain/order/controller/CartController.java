package com.sparta.nationofdevelopment.domain.order.controller;

import com.sparta.nationofdevelopment.common_entity.ApiResponse;
import com.sparta.nationofdevelopment.domain.common.annotation.Auth;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.order.dto.requestDto.MenuItemDto;
import com.sparta.nationofdevelopment.domain.order.dto.responseDto.CartDto;
import com.sparta.nationofdevelopment.domain.order.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/stores/{storeId}/cart")
    public ApiResponse<CartDto> create(@Auth AuthUser authUser, @RequestBody MenuItemDto requestDto,@PathVariable long storeId) {
        return ApiResponse.onSuccess(cartService.createCart(requestDto,authUser, storeId));
    }

    @DeleteMapping("/users/cart/{cartId}")
    public ApiResponse<String> delete(@Auth AuthUser authUser, @PathVariable long cartId) {
        return ApiResponse.onSuccess(cartService.deleteCart(authUser,cartId));
    }

    @GetMapping("/users/cart")
    public ApiResponse<List<CartDto>> findCart(@Auth AuthUser authUser) {
        return ApiResponse.onSuccess(cartService.findCart(authUser));
    }

}
