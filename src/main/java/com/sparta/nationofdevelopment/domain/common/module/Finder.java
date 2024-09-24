package com.sparta.nationofdevelopment.domain.common.module;

import com.sparta.nationofdevelopment.common_entity.ErrorStatus;
import com.sparta.nationofdevelopment.domain.common.exception.ApiException;
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

    public Users findByUsersId(long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ApiException(ErrorStatus._BAD_REQUEST_NOT_FOUND_USER)
        );
    }

    public Store findByStoreId(long storeId) {
        return storeRepository.findById(storeId).orElseThrow(
                ()->new ApiException(ErrorStatus._NOT_FOUND_STORE)
        );
    }

    public Orders findByOrderId(long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
                ()->new ApiException(ErrorStatus._BAD_REQUEST_NOT_FOUND_ORDER)
        );
    }




}
