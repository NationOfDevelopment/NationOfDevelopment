package com.sparta.nationofdevelopment.domain.menu.controller;

import com.sparta.nationofdevelopment.common_entity.ApiResponse;
import com.sparta.nationofdevelopment.domain.common.annotation.Auth;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.menu.dto.MenuRequestDto;
import com.sparta.nationofdevelopment.domain.menu.dto.MenuResponseDto;
import com.sparta.nationofdevelopment.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @PostMapping("/stores/{store_id}/menus")
    public ApiResponse<MenuResponseDto> saveMenu(@Auth AuthUser authUser, @PathVariable Long store_id, @RequestBody MenuRequestDto requestDto) {
        return ApiResponse.onSuccess(menuService.saveMenu(authUser, store_id, requestDto));
    }

    @PutMapping("/stores/{store_id}/menus/{menu_id}")
    public ApiResponse<MenuResponseDto> updateMenu(@Auth AuthUser authUser,
                                                    @PathVariable Long store_id,
                                                    @PathVariable Long menu_id,
                                                    @RequestBody MenuRequestDto requestDto) {
        return ApiResponse.onSuccess(menuService.updatemenu(authUser, store_id, menu_id, requestDto));
    }

    @DeleteMapping("/stores/{store_id}/menus/{menu_id}")
    public ApiResponse<Void> deleteMenu (@Auth AuthUser authUser,
                                        @PathVariable Long store_id,
                                        @PathVariable Long menu_id) {
        menuService.deleteMenu(authUser, store_id, menu_id);
        return ApiResponse.onSuccess(null);
    }
}
