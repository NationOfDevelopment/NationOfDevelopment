package com.sparta.nationofdevelopment.domain.common.module;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Pointcut;

import java.time.LocalDateTime;

@Slf4j
@org.aspectj.lang.annotation.Aspect
public class Aspect {
    //Order 관련 포인트컷
    @Pointcut("execution(* com.sparta.nationofdevelopment.domain.order.service.OrderService.*(..))")
    public void orderServiceMethods() {}

    /**
     * Order 관련 어드바이스
     * 메서스 접근 시 요청 시각, 가게 ID, 주문 ID 로깅
     */
    @After("orderServiceMethods()")
    public void orderLogging(JoinPoint joinPoint) {
        //파라미터 받아오기
        /**
         * 1.주문이 생성될 때 어떻게 할 것인가?
         * 생성되고 나면 저 서비스 메서드에서
         */


        LocalDateTime currentTime = LocalDateTime.now();
        //요청 시각
        log.info("요청 시각 : {}",currentTime);
        //가게 ID
        log.info("가게 ID : {storeId}");
        //주문 ID
        log.info("주문 ID : {orderId}");
    }


}
