package com.sparta.nationofdevelopment.domain.store.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.GeneratedValue;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class StoreRequestDto {

    private String storeName;
    @JsonFormat(pattern = "HH:mm") // 시간 형식 지정
    private LocalTime openTime;
    @JsonFormat(pattern = "HH:mm") // 시간 형식 지정
    private LocalTime closeTime;
    private int minOrderMount;


}
