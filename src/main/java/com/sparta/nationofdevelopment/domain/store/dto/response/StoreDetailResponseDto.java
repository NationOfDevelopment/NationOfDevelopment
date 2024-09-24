package com.sparta.nationofdevelopment.domain.store.dto.response;

import com.sparta.nationofdevelopment.domain.menu.dto.MenuResponseDto;
import com.sparta.nationofdevelopment.domain.store.entity.Store;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@NoArgsConstructor
public class StoreDetailResponseDto {
    private Long storeId;
    private String storeName;
    private LocalTime openTime;
    private LocalTime closeTime;
    private int minOrderMount;
    private List<MenuResponseDto> menuList;


    public StoreDetailResponseDto(Store store) {
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.openTime = store.getOpenTime();
        this.closeTime = store.getCloseTime();
        this.minOrderMount = store.getMinOrderMount();
        this.menuList = store.getMenus().stream()
                .map(menu -> new MenuResponseDto(
                        menu.getId(),
                        menu.getMenuName(),
                        menu.getAmount(),
                        menu.getCategory()
                ))
                .collect(Collectors.toList());
        // menu entity가 변경되면, 수정해야하기 때문에 dto사용
    }
}
