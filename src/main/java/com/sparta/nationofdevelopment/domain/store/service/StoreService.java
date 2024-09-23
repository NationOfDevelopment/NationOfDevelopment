package com.sparta.nationofdevelopment.domain.store.service;


import com.sparta.nationofdevelopment.common_entity.ApiResponse;
import com.sparta.nationofdevelopment.domain.common.dto.AuthUser;
import com.sparta.nationofdevelopment.domain.store.dto.request.StoreClosureRequestDto;
import com.sparta.nationofdevelopment.domain.store.dto.request.StoreRequestDto;
import com.sparta.nationofdevelopment.domain.store.dto.response.StoreDetailResponseDto;
import com.sparta.nationofdevelopment.domain.store.dto.response.StoreResponseDto;
import com.sparta.nationofdevelopment.domain.store.entity.Store;
import com.sparta.nationofdevelopment.domain.store.entity.StoreStatus;
import com.sparta.nationofdevelopment.domain.store.repository.StoreRepository;
import com.sparta.nationofdevelopment.domain.user.entity.Users;
import com.sparta.nationofdevelopment.domain.user.enums.UserRole;
import com.sparta.nationofdevelopment.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

    @Transactional
    public StoreResponseDto postStore(StoreRequestDto requestDto, AuthUser authUser) {
        // 인증된 사용자 가져오기
        Users users = userRepository.findByEmail(authUser.getEmail()).orElseThrow(()
                -> new IllegalArgumentException("인증된 사용자 이메일이 존재하지 않습니다."));

        // 권한 체크
        if (!users.getUserRole().equals(UserRole.ADMIN)) {
            throw new IllegalArgumentException("사장님 권한이 없습니다.");
        }


        // 운영하는 가게 3개 초과 체크
        if (storeRepository.countByUserAndStatus(users,StoreStatus.OPEN) >= 3) {
            throw new IllegalArgumentException("최대 3개 운영가능");
        }

        Store store = new Store(requestDto, users);

        Store save = storeRepository.save(store);

        return new StoreResponseDto(save);

    }


    @Transactional
    public StoreResponseDto updateStore(AuthUser authUser, Long storeId,
                                        StoreRequestDto requestDto) {

        Store store = storeRepository.findById(storeId).orElseThrow(()
                -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        Users users = userRepository.findByEmail(authUser.getEmail()).orElseThrow(()
                -> new IllegalArgumentException("인증된 사용자 이메일이 존재하지 않습니다."));

        if (!users.getUserRole().equals(UserRole.ADMIN)) {
            throw new IllegalArgumentException("사장님 권한이 없습니다.");
        }

        // 가게 정보 업데이트
        store.setStoreName(requestDto.getStoreName());
        store.setOpenTime(requestDto.getOpenTime());
        store.setCloseTime(requestDto.getCloseTime());
        store.setMinOrderMount(requestDto.getMinOrderMount());

        Store updatedStore = storeRepository.save(store);

        return new StoreResponseDto(updatedStore);
    }



    // 가게 단건 조회
    public StoreDetailResponseDto getStore(Long storeId){
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("storeId를 찾을 수 없습니다."));

        // 폐업 상태 확인
        if(store.getStatus() == StoreStatus.CLOSED){
            throw new EntityNotFoundException("StoreId가 CLOSE 상태입니다.");
        }
        return new StoreDetailResponseDto(store);
    }


    // 가게 다건 조회
    public List<StoreResponseDto> getStores(String storeName) {
        List<Store> stores = storeRepository.findByStoreName(storeName);

        return stores.stream()
                .map(StoreResponseDto::new)
                .collect(Collectors.toList());
    }


    @Transactional
    public StoreResponseDto deleteStore(Long storeId){

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("StoreId를 찾을 수 없습니다."));

        store.closeStore();
        storeRepository.save(store);

        return new StoreResponseDto(store);
    }
}

