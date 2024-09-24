package com.sparta.nationofdevelopment.domain.common.module;

import com.sparta.nationofdevelopment.domain.order.entity.Cart;
import com.sparta.nationofdevelopment.domain.order.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class Scheduler {
    private final CartRepository cartRepository;

    //1시간마다 "주문이 확정되지 않고 24시간이 지난 장바구니" 삭제
    @Transactional
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void runCartCleaner() {
        LocalDateTime timeLimit = LocalDateTime.now().minusHours(24);
        log.info("만료된 장바구니를 삭제합니다..");
        List<Cart> cartsToDelete = cartRepository.findInvalidCarts(timeLimit);
        log.info("찾은 장바구니 수 : {}", cartsToDelete.size());
        cartRepository.deleteAll(cartsToDelete);
        log.info("삭제 완료");
    }
}
