package com.sparta.nationofdevelopment.domain.order.service;

import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.common.exception.InvalidRequestException;
import com.sparta.nationofdevelopment.domain.menu.entity.Menu;
import com.sparta.nationofdevelopment.domain.menu.repository.MenuRepository;
import com.sparta.nationofdevelopment.domain.order.dto.MenuItemDto;
import com.sparta.nationofdevelopment.domain.order.dto.OrderRequestDto;
import com.sparta.nationofdevelopment.domain.order.entity.Cart;
import com.sparta.nationofdevelopment.domain.order.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final MenuRepository menuRepository;
    private final CartRepository cartRepository;

    /**
     * OrderRequestDto에서 장바구니 리스트 반환
     */
    @Transactional
    public List<Cart> convertToCartList(OrderRequestDto requestDto, AuthUser authUser) {
        List<Cart> cartList = new ArrayList<>();
        List<MenuItemDto> menuItemDtos = requestDto.getMenuItems();
        for (MenuItemDto dto : menuItemDtos) {
            Menu menu = menuRepository.findByMenuName(dto.getMenuName()).orElseThrow(() -> new InvalidRequestException("해당 메뉴는 존재하지 않습니다."));
            Cart cart = new Cart(dto, authUser, menu);
            Cart savedCart = cartRepository.save(cart);
            cartList.add(savedCart);
        }

        return cartList;
    }
}
