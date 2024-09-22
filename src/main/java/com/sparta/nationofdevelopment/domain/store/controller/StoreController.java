package com.sparta.nationofdevelopment.domain.store.controller;

import com.sparta.nationofdevelopment.common_entity.ApiResponse;
import com.sparta.nationofdevelopment.domain.common.annotation.Auth;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.store.dto.request.StoreClosureRequestDto;
import com.sparta.nationofdevelopment.domain.store.dto.request.StoreRequestDto;
import com.sparta.nationofdevelopment.domain.store.dto.response.StoreDetailResponseDto;
import com.sparta.nationofdevelopment.domain.store.dto.response.StoreResponseDto;
import com.sparta.nationofdevelopment.domain.store.entity.Store;
import com.sparta.nationofdevelopment.domain.store.service.StoreService;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/stores")
    public ApiResponse<StoreResponseDto> postStore(@Auth AuthUser authUser,
                                                      @Valid @RequestBody StoreRequestDto requestDto) {

        StoreResponseDto storeResponseDto = storeService.postStore(requestDto, authUser);
        return ApiResponse.onSuccess(storeResponseDto);
    }

    @PutMapping("/stores/{storeId}")
    public ApiResponse<StoreResponseDto> updateStore(@Auth AuthUser authUser,
                                                     @Valid @PathVariable Long storeId,
                                                     @RequestBody StoreRequestDto requestDto){

        StoreResponseDto storeResponseDto = storeService.updateStore(authUser,storeId,requestDto);
        return ApiResponse.onSuccess(storeResponseDto);
    }

    // 가게 단건 조회
    @GetMapping("/stores/{storeId}")
    public ApiResponse<StoreDetailResponseDto> getStore(@PathVariable Long storeId) {
        StoreDetailResponseDto storeDetailResponseDto = storeService.getStore(storeId);
        return ApiResponse.onSuccess(storeDetailResponseDto);
    }


    // 가게 다건 조회
    @GetMapping("/stores")
    public ApiResponse<List<StoreResponseDto>> getStores(@RequestParam String storeName){
        List<StoreResponseDto> storeResponseDto = storeService.getStores(storeName);
        return ApiResponse.onSuccess(storeResponseDto);
    }


    @DeleteMapping("/stores/{storeId}/close")
    public ApiResponse<StoreResponseDto> deleteStore(@PathVariable Long storeId){

        StoreResponseDto storeResponseDto = storeService.deleteStore(storeId);
        return ApiResponse.onSuccess(storeResponseDto);
    }


}
