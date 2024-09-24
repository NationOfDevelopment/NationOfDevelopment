package com.sparta.nationofdevelopment.domain.common.module;

import com.sparta.nationofdevelopment.common_entity.ErrorStatus;
import com.sparta.nationofdevelopment.domain.common.exception.ApiException;
import com.sparta.nationofdevelopment.domain.menu.entity.Menu;
import com.sparta.nationofdevelopment.domain.menu.repository.MenuRepository;
import com.sparta.nationofdevelopment.domain.order.entity.Cart;
import com.sparta.nationofdevelopment.domain.order.entity.Orders;
import com.sparta.nationofdevelopment.domain.order.repository.CartRepository;
import com.sparta.nationofdevelopment.domain.order.repository.OrderRepository;
import com.sparta.nationofdevelopment.domain.store.entity.Store;
import com.sparta.nationofdevelopment.domain.store.repository.StoreRepository;
import com.sparta.nationofdevelopment.domain.user.entity.Users;
import com.sparta.nationofdevelopment.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Finder {
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final MenuRepository menuRepository;

    public Users findUserByUsersId(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ApiException(ErrorStatus._BAD_REQUEST_NOT_FOUND_USER)
        );
    }

    public Store findStoreByStoreId(long storeId) {
        return storeRepository.findById(storeId).orElseThrow(
                ()->new ApiException(ErrorStatus._NOT_FOUND_STORE)
        );
    }

    public Orders findOrderByOrderId(long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                ()->new ApiException(ErrorStatus._BAD_REQUEST_NOT_FOUND_ORDER)
        );
    }

    public Menu findMenuByMenuNameAndStoreId(String menuName, long storeId) {
        return menuRepository.findByMenuNameAndStore_storeId(menuName, storeId).orElseThrow(
                ()->new ApiException(ErrorStatus._NOT_FOUND_MENU)
        );
    }

    public Cart findCartByCartId(long cartId) {
        return cartRepository.findById(cartId).orElseThrow(
                ()-> new ApiException(ErrorStatus._BAD_REQUEST_NOT_FOUND_CART)
        );

    }




}
