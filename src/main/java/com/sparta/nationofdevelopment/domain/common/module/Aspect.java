package com.sparta.nationofdevelopment.domain.common.module;

import com.sparta.nationofdevelopment.domain.order.dto.OrderResponseDto;
import com.sparta.nationofdevelopment.domain.order.dto.OrderStatusResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.time.LocalDateTime;

@Slf4j
@org.aspectj.lang.annotation.Aspect
public class Aspect {
    //Order 관련 포인트컷
    @Pointcut("execution(* com.sparta.nationofdevelopment.domain.order.service.OrderService.*(..))")
    public void orderServiceMethods() {}

    /**
     * 주문 관련 로깅
     * 메서스 호출 전 요청 시각, 호출 후 가게 ID, 주문 ID 로깅
     */
    @Before("orderServiceMethods()")
    public void orderRequestTimeLogging() {
        LocalDateTime currentTime = LocalDateTime.now();
        //요청 시각
        log.info("요청 시각 : {}",currentTime);
    }

    @AfterReturning(pointcut = "orderServiceMethods().create(..)", returning  = "orderResponse")
    public void orderRequestLogging(OrderResponseDto orderResponse) {
        log.info("가게 ID : {}",orderResponse.getStoreId());
        log.info("주문 ID : {}",orderResponse.getOrderId());
    }

    @AfterReturning(pointcut = "orderServiceMethods().changeStatus(..)", returning  = "orderResponse")
    public void orderChangeStatusLogging(OrderStatusResponseDto orderResponse) {
        log.info("가게 ID : {}",orderResponse.getStoreId());
        log.info("주문 ID : {}",orderResponse.getOrderId());
    }




}
