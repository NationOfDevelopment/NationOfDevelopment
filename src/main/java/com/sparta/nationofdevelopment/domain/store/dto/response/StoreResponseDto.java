package com.sparta.nationofdevelopment.domain.store.dto.response;

import com.sparta.nationofdevelopment.domain.store.entity.Store;
import com.sparta.nationofdevelopment.domain.store.entity.StoreStatus;
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
    private StoreStatus status;

    public StoreResponseDto(Store store) {
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.openTime = store.getOpenTime();
        this.closeTime = store.getCloseTime();
        this.minOrderMount = store.getMinOrderMount();
        this.status = store.getStatus();
    }
}
