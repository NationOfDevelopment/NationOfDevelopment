package com.sparta.nationofdevelopment.domain.menu.service;

import com.sparta.nationofdevelopment.common_entity.ErrorStatus;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.common.exception.ApiException;
import com.sparta.nationofdevelopment.domain.menu.dto.MenuRequestDto;
import com.sparta.nationofdevelopment.domain.menu.dto.MenuResponseDto;
import com.sparta.nationofdevelopment.domain.menu.entity.Menu;
import com.sparta.nationofdevelopment.domain.menu.repository.MenuRepository;
import com.sparta.nationofdevelopment.domain.menu.util.UtilFind;
import com.sparta.nationofdevelopment.domain.store.entity.Store;
import com.sparta.nationofdevelopment.domain.store.repository.StoreRepository;
import com.sparta.nationofdevelopment.domain.user.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final UtilFind utilFind;

    /**
     * 메뉴 등록
     *
     * @param authUser
     * @param storeId
     * @param requestDto
     * @return
     */
    @Transactional
    public MenuResponseDto saveMenu(AuthUser authUser, Long storeId, MenuRequestDto requestDto) {
        validate(authUser, storeId, ErrorStatus._AUTH_OWNER_MENU);

        Menu newMenu = new Menu(requestDto, utilFind.storeFindById(storeId));
        Menu savedMenu = menuRepository.save(newMenu);

        return new MenuResponseDto(
                savedMenu.getId(),
                savedMenu.getMenuName(),
                savedMenu.getAmount(),
                savedMenu.getCategory()
        );
    }

    /**
     * 메뉴 수정
     *
     * @param authUser
     * @param storeId
     * @param menuId
     * @param requestDto
     * @return
     */
    public MenuResponseDto updatemenu(AuthUser authUser, Long storeId, Long menuId, MenuRequestDto requestDto) {
        validate(authUser, storeId, ErrorStatus._AUTH_OWNER_MENU);

        // 메뉴가 없을 때
        Menu menu = utilFind.menuFindById(menuId);

        menu.update(requestDto);
        Menu updatedMenu = menuRepository.save(menu);

        return new MenuResponseDto(
                updatedMenu.getId(),
                updatedMenu.getMenuName(),
                updatedMenu.getAmount(),
                updatedMenu.getCategory()
        );
    }

    /**
     * 메뉴 삭제
     *
     * @param authUser
     * @param menuId
     * @param storeId
     */
    @Transactional
    public void deleteMenu(AuthUser authUser, Long menuId, Long storeId) {
        validate(authUser, storeId, ErrorStatus._AUTH_OWNER_MENU_DELETED);

        // 메뉴가 없을 때
        Menu menu = utilFind.menuFindById(menuId);

        menu.delete();
        menuRepository.save(menu);
    }





    private void validate(AuthUser authUser, Long storeId, ErrorStatus authErrorStatus) {
        // 관리자 권한 체크
        if (!authUser.getUserRole().equals(UserRole.OWNER)) {
            throw new ApiException(authErrorStatus);
        }

        // 가게가 없을 때
        Store store = utilFind.storeFindById(storeId);

        // 가게 사장님인지 확인
        if (!store.getUser().getId().equals(authUser.getId())) {
            throw new ApiException(ErrorStatus._UNAUTHORIZED_STORE_ACCESS);
        }
    }



}
