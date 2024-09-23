package com.sparta.nationofdevelopment.domain.store.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.nationofdevelopment.domain.store.entity.StoreStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class StoreRequestDto {

    private String storeName;
    @JsonFormat(pattern = "HH:mm") // 시간 형식 지정
    private LocalTime openTime;
    @JsonFormat(pattern = "HH:mm") // 시간 형식 지정
    private LocalTime closeTime;
    private int minOrderMount;
    private StoreStatus status;
}
