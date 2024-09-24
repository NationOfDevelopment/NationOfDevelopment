package com.sparta.nationofdevelopment.domain.menu.util;

import com.sparta.nationofdevelopment.common_entity.ErrorStatus;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.common.exception.ApiException;
import com.sparta.nationofdevelopment.domain.menu.entity.Menu;
import com.sparta.nationofdevelopment.domain.menu.repository.MenuRepository;
import com.sparta.nationofdevelopment.domain.store.entity.Store;
import com.sparta.nationofdevelopment.domain.store.repository.StoreRepository;
import com.sparta.nationofdevelopment.domain.user.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UtilFind {
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    /**
     * id로 가게 찾는 메서드
     *
     * @param storeId 찾는 가게 id
     * @return
     * @author tiyu
     */
    public Store storeFindById(Long storeId) {
        return storeRepository.findById(storeId).orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_STORE));
    }

    /**
     * id로 메뉴 찾는 메서드
     * @param menuId 찾는 메뉴 id
     * @return
     * @author tiyu
     */
    public Menu menuFindById(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_MENU));
    }
}
