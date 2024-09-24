package com.sparta.nationofdevelopment.authtest;

import com.sparta.nationofdevelopment.common_entity.ErrorStatus;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.common.exception.ApiException;
import com.sparta.nationofdevelopment.domain.menu.dto.MenuRequestDto;
import com.sparta.nationofdevelopment.domain.menu.dto.MenuResponseDto;
import com.sparta.nationofdevelopment.domain.menu.entity.Menu;
import com.sparta.nationofdevelopment.domain.menu.enums.MenuStatus;
import com.sparta.nationofdevelopment.domain.menu.repository.MenuRepository;
import com.sparta.nationofdevelopment.domain.menu.service.MenuService;
import com.sparta.nationofdevelopment.domain.store.dto.request.StoreRequestDto;
import com.sparta.nationofdevelopment.domain.store.entity.Store;
import com.sparta.nationofdevelopment.domain.store.repository.StoreRepository;
import com.sparta.nationofdevelopment.domain.user.entity.Users;
import com.sparta.nationofdevelopment.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuTest {
    @Mock
    MenuRepository menuRepository;

    @Mock
    StoreRepository storeRepository;


    @InjectMocks
    MenuService menuService;

    /**
     * 메뉴 등록 테스트
     */

    @Test
    void 메뉴_등록_시_로그인_한_유저가_사장님인지() {
        AuthUser authUser = new AuthUser(1L, "test@naver.com", "123", UserRole.USER);
        Long storeId = 1L;
        MenuRequestDto requestDto = new MenuRequestDto("메뉴 이름", 14000, "한식");

        ApiException e = assertThrows(ApiException.class, () -> {
           menuService.saveMenu(authUser, storeId, requestDto);
        });

        assertEquals("메뉴 생성 및 수정은 사장님만 가능합니다.", e.getErrorCode().getReasonHttpStatus().getMessage());
    }

    /*@Test
    void 메뉴_등록_시_로그인_한_유저가_사장님일_때() {
        AuthUser authUser = new AuthUser(1L, "test@naver.com", "123", UserRole.ADMIN);
        Long storeId = 1L;
        MenuRequestDto requestDto = new MenuRequestDto("메뉴 이름", 14000, "한식");
        Menu saveMenu = new Menu(requestDto);

        given(storeRepository.findById(storeId)).willReturn(Optional.of(new Store()));
        given(menuRepository.save(any(Menu.class))).willReturn(saveMenu);

        assertDoesNotThrow(() -> {
            menuService.saveMenu(authUser, storeId, requestDto);
        });
    }*/

    @Test
    void 메뉴_등록_시_가게가_없을_때() {
        AuthUser authUser = new AuthUser(1L, "test@naver.com", "123", UserRole.ADMIN);
        Long storeId = 999L;
        MenuRequestDto requestDto = new MenuRequestDto("메뉴 이름", 14000, "한식");

        given(storeRepository.findById(storeId)).willReturn(Optional.empty());

        ApiException e = assertThrows(ApiException.class, () -> {
            menuService.saveMenu(authUser, storeId, requestDto);
        });

        assertEquals("가게를 찾을 수 없습니다.", e.getErrorCode().getReasonHttpStatus().getMessage());
    }

    /*@Test
    void 메뉴_등록_성공() {
        long storeId = 1L;

        AuthUser authUser = new AuthUser(1L, "test@naver.com", "123", UserRole.ADMIN);
        Users user = Users.fromAuthUser(authUser);

        MenuRequestDto requestDto = new MenuRequestDto("메뉴 이름", 14000, "한식");
        Menu saveMenu = new Menu(requestDto);

        given(storeRepository.findById(storeId)).willReturn(Optional.of(new Store()));
        given(menuRepository.save(any(Menu.class))).willReturn(saveMenu);

        MenuResponseDto responseDto = menuService.saveMenu(authUser, storeId, requestDto);

        assertEquals(saveMenu.getId(), responseDto.getId());
        assertEquals(saveMenu.getMenuName(), responseDto.getMenuName());
        assertEquals(saveMenu.getAmount(), responseDto.getAmount());
        assertEquals(saveMenu.getCategory(), responseDto.getCategory());
    }*/

    /**
     * 메뉴 수정 테스트
     */
    @Test
    void 메뉴_수정_시_해당_메뉴를_찾을_수_없을_때() {
        AuthUser authUser = new AuthUser(1L, "test@naver.com", "123", UserRole.ADMIN);
        long menuId = 1L;
        long storeId = 1L;

        MenuRequestDto requestDto = new MenuRequestDto("메뉴 이름", 14000, "한식");

        given(storeRepository.findById(storeId)).willReturn(Optional.of(new Store()));
        given(menuRepository.findById(menuId)).willReturn(Optional.empty());

        ApiException e = assertThrows(ApiException.class, () ->
                menuService.updatemenu(authUser, storeId, menuId, requestDto)
        );

        assertEquals("해당 메뉴를 찾을 수 없습니다.", e.getErrorCode().getReasonHttpStatus().getMessage());
    }

    /*@Test
    void 메뉴_수정_성공() {
        long storeId = 1L;
        long menuId = 1L;

        AuthUser authUser = new AuthUser(1L, "test@naver.com", "123", UserRole.ADMIN);

        // 가게 생성
        Store store = new Store();

        // 기존 메뉴
        Menu prevMenu = new Menu("기존 메뉴 이름", 15000, "중식", menuId);

        // 메뉴 수정
        MenuRequestDto requestDto = new MenuRequestDto("수정 된 메뉴 이름", 16000, "양식");
        Menu updateMenu = new Menu(requestDto.getMenuName(), requestDto.getAmount(), requestDto.getCategory(), menuId);

        given(storeRepository.findById(storeId)).willReturn(Optional.of(store));
        given(menuRepository.findById(menuId)).willReturn(Optional.of(prevMenu));
        given(menuRepository.save(prevMenu)).willReturn(updateMenu);

        MenuResponseDto responseDto = menuService.updatemenu(authUser, storeId, menuId, requestDto);

        assertEquals(updateMenu.getId(), responseDto.getId());
        assertEquals(updateMenu.getMenuName(), responseDto.getMenuName());
        assertEquals(updateMenu.getAmount(), responseDto.getAmount());
        assertEquals(updateMenu.getCategory(), responseDto.getCategory());
    }*/

    /**
     * 메뉴 삭제 테스트
     */
    /*@Test
    void 메뉴_삭제_성공() {
        // Given
        AuthUser authUser = new AuthUser(1L, "test@naver.com", "123", UserRole.ADMIN);
        Long menuId = 1L;
        Long storeId = 1L;
        Menu menu = new Menu("메뉴 이름", 14000, "한식", storeId);
        menu.delete(); // 초기 상태를 DELETED로 설정

        given(menuRepository.findById(menuId)).willReturn(Optional.of(menu));
        given(storeRepository.findById(storeId)).willReturn(Optional.of(new Store()));

        // When
        menuService.deleteMenu(authUser, menuId, storeId);

        // Then
        assertEquals(MenuStatus.DELETED, menu.getState()); // 상태가 DELETED인지 확인
    }*/



}
