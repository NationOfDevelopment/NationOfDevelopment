package com.sparta.nationofdevelopment.config;

import com.sparta.nationofdevelopment.domain.common.module.Aspect;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    //Aspect 등록
    @Bean
    public Aspect getAspectAop() {
        return new Aspect();
    }
}
