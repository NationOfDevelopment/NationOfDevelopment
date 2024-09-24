package com.sparta.nationofdevelopment.domain.common.module;

import com.sparta.nationofdevelopment.domain.order.entity.Cart;
import com.sparta.nationofdevelopment.domain.order.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchedulerTest {
    @Mock
    CartRepository cartRepository;

    @Mock
    Clock clock;

    @InjectMocks
    Scheduler scheduler;

    @Test
    void 장바구니_클리너_테스트() {
        // 고정된 시간 설정
        Clock fixedClock = Clock.fixed(Instant.parse("2024-09-25T21:27:19Z"), ZoneId.systemDefault());
        when(clock.instant()).thenReturn(fixedClock.instant());
        when(clock.getZone()).thenReturn(fixedClock.getZone());

        List<Cart> mockCarts = List.of(new Cart(), new Cart());
        when(cartRepository.findInvalidCarts(any())).thenReturn(mockCarts);

        scheduler.runCartCleaner();

        // 삭제된 데이터 확인
        verify(cartRepository, times(1)).deleteAll(mockCarts);
        assertEquals(2, mockCarts.size());
    }
}