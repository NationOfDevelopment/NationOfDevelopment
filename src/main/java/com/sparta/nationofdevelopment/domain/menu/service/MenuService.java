package com.sparta.nationofdevelopment.domain.menu.service;

import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.menu.dto.MenuRequestDto;
import com.sparta.nationofdevelopment.domain.menu.dto.MenuResponseDto;
import com.sparta.nationofdevelopment.domain.menu.entity.Menu;
import com.sparta.nationofdevelopment.domain.menu.repository.MenuRepository;
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

    @Transactional
    public MenuResponseDto saveMenu(AuthUser authUser, Long storeId, MenuRequestDto requestDto) {

        if(!authUser.getUserRole().equals(UserRole.ADMIN)) {
            throw new IllegalArgumentException("사장님만 메뉴를 생성할 수 있습니다.");
        }

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

//        if(!store.getUser().equals(authUser.getId())) {
//            throw new IllegalArgumentException("본인 가게에만 메뉴를 등록할 수 있습니다.");
//        }

        Menu newMenu = new Menu(requestDto);
        Menu savedMenu = menuRepository.save(newMenu);
        return new MenuResponseDto(
                savedMenu.getId(),
                savedMenu.getMenuName(),
                savedMenu.getAmount(),
                savedMenu.getCategory()
        );
    }

    public MenuResponseDto updatemenu(AuthUser authUser,
                                      Long storeId,
                                      Long menuId,
                                      MenuRequestDto requestDto) {

        if(!authUser.getUserRole().equals(UserRole.ADMIN)) {
            throw new IllegalArgumentException("사장님만 메뉴를 수정할 수 있습니다.");
        }

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴를 찾을 수 없습니다."));

        menu.update(requestDto);
        Menu updatedMenu = menuRepository.save(menu);
        return new MenuResponseDto(
                updatedMenu.getId(),
                updatedMenu.getMenuName(),
                updatedMenu.getAmount(),
                updatedMenu.getCategory()
        );
    }

    @Transactional
    public void deleteMenu(AuthUser authUser, Long menuId, Long storeId) {
        if(!authUser.getUserRole().equals(UserRole.ADMIN)) {
            throw new IllegalArgumentException("사장님만 메뉴를 삭제할 수 있습니다.");
        }

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴를 찾을 수 없습니다."));

        menuRepository.delete(menu);
    }
}
