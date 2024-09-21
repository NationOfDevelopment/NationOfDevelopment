package com.sparta.nationofdevelopment.domain.store.dto.response;

import com.sparta.nationofdevelopment.domain.store.entity.Store;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class StoreResponseDto {
    private Long storeId;
    private String storeName;
    private LocalTime openTime;
    private LocalTime closeTime;
    private int minOrderMount;
    public StoreResponseDto(Store store) {
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.openTime = store.getOpenTime();
        this.closeTime = store.getCloseTime();
        this.minOrderMount = store.getMinOrderMount();
    }
    }
