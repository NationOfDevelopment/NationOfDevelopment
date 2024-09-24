package com.sparta.nationofdevelopment.config;

import com.sparta.nationofdevelopment.domain.common.module.Aspect;
import com.sparta.nationofdevelopment.domain.common.module.Aspect;
import com.sparta.nationofdevelopment.domain.common.module.Finder;
import com.sparta.nationofdevelopment.domain.menu.repository.MenuRepository;
import com.sparta.nationofdevelopment.domain.order.repository.CartRepository;
import com.sparta.nationofdevelopment.domain.order.repository.OrderRepository;
import com.sparta.nationofdevelopment.domain.store.repository.StoreRepository;
import com.sparta.nationofdevelopment.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Clock;
import java.util.List;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    // ArgumentResolver 등록
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthUserArgumentResolver());
    }

    //Aspect 등록
    @Bean
    public Aspect getAspectAop() {
        return new Aspect();
    }

    //Clock 등록
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    //Finder 등록
    @Bean
    public Finder finder(OrderRepository orderRepository, StoreRepository storeRepository, CartRepository cartRepository, UserRepository userRepository, MenuRepository menuRepository) {
        return new Finder(orderRepository,storeRepository,cartRepository,userRepository,menuRepository);
    }
}
