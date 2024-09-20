package com.sparta.nationofdevelopment.domain.common.module;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Pointcut;

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

    }


}
