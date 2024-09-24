package com.sparta.nationofdevelopment.domain.review.dto.response;

import com.sparta.nationofdevelopment.domain.store.entity.Store;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReviewStoreResponseDto {
    private Long storeId;
    private String storeName;

    public static ReviewStoreResponseDto of(Store store) {
        ReviewStoreResponseDto responseDto = new ReviewStoreResponseDto();
        responseDto.storeId = store.getStoreId();
        responseDto.storeName = store.getStoreName();
        return responseDto;
    }
}
