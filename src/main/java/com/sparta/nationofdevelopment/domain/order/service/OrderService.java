package com.sparta.nationofdevelopment.domain.order.service;

import com.sparta.nationofdevelopment.domain.common.exception.InvalidRequestException;
import com.sparta.nationofdevelopment.domain.order.dto.OrderResponseDto;
import com.sparta.nationofdevelopment.domain.order.repository.OrderRepository;
import com.sparta.nationofdevelopment.domain.store.entity.Store;
import com.sparta.nationofdevelopment.domain.store.repository.StoreRepository;
import com.sparta.nationofdevelopment.domain.user.entity.Users;
import com.sparta.nationofdevelopment.domain.user.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    /**
     * 대충 임시 AuthUser
     */


    /**
     * 주문 생성
     * 1. 주문 : 주문Id, 유저Id, 가게Id, 총 가격, 주문 시간 , status 주문 요청으로 변경
     */
    public OrderResponseDto create(Users user, long storeId) {
        Store foundStore = storeRepository.findById(storeId).orElseThrow(()-> new InvalidRequestException("해당 가게를 찾을 수 없습니다."));
    }


}
